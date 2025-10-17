package com.kurnia.atmmini

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val requestPerm = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
        // no-op
    }

    private lateinit var adapterSpinner: Spinner
    private lateinit var btnConnect: Button
    private lateinit var btnPrint: Button
    private lateinit var btnTopUp: Button
    private lateinit var btnRefresh: Button
    private val btAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }
    private var selectedDevice: BluetoothDevice? = null
    private var printer: BluetoothPrinter? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // request recommended runtime permissions (for Android 12+ bluetooth)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPerm.launch(arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            requestPerm.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }

        adapterSpinner = findViewById(R.id.spinnerDevices)
        btnConnect = findViewById(R.id.btnConnect)
        btnPrint = findViewById(R.id.btnPrint)
        btnTopUp = findViewById(R.id.btnTopUp)
        btnRefresh = findViewById(R.id.btnRefresh)
        val logo = findViewById<ImageView>(R.id.logoImage)
        logo.setImageResource(R.drawable.logo_kurnia)

        btnRefresh.setOnClickListener { listPairedDevices() }
        btnConnect.setOnClickListener {
            val dev = selectedDevice
            if (dev == null) {
                Toast.makeText(this, "Pilih perangkat terlebih dahulu.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            printer = BluetoothPrinter(dev)
            Thread {
                val ok = printer?.connect() ?: false
                runOnUiThread {
                    Toast.makeText(this, if (ok) "Terhubung ke printer" else "Gagal terhubung", Toast.LENGTH_SHORT).show()
                }
            }.start()
        }

        btnPrint.setOnClickListener {
            if (printer == null || !printer!!.isConnected()) {
                Toast.makeText(this, "Printer belum tersambung.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Thread {
                printer?.printSample(ReceiptMaker.sampleReceipt())
            }.start()
        }

        btnTopUp.setOnClickListener {
            if (printer == null || !printer!!.isConnected()) {
                Toast.makeText(this, "Printer belum tersambung.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Thread {
                val receipt = ReceiptMaker.topupReceipt("081234567890", 50000)
                printer?.printSample(receipt)
            }.start()
        }

        adapterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("MissingPermission")
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val item = parent?.getItemAtPosition(position) as? String ?: return
                val parts = item.split("\n".toRegex()).map { it.trim() }
                val addr = parts.lastOrNull()
                if (addr != null && btAdapter != null) {
                    selectedDevice = btAdapter!!.bondedDevices.firstOrNull { it.address == addr }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { selectedDevice = null }
        }

        // initial list
        listPairedDevices()
    }

    @SuppressLint("MissingPermission")
    private fun listPairedDevices() {
        val paired = btAdapter?.bondedDevices
        val list = ArrayList<String>()
        if (paired != null && paired.isNotEmpty()) {
            for (d in paired) {
                list.add("${d.name}\n${d.address}")
            }
        } else {
            list.add("Tidak ada perangkat terpasang")
        }
        runOnUiThread {
            adapterSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        }
    }
}
