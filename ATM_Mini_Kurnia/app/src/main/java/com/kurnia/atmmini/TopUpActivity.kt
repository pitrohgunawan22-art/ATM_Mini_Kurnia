package com.kurnia.atmmini

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TopUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup)

        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val edtAmount = findViewById<EditText>(R.id.edtAmount)
        val btnDo = findViewById<Button>(R.id.btnDoTopup)

        btnDo.setOnClickListener {
            val phone = edtPhone.text.toString().trim()
            val amt = edtAmount.text.toString().toIntOrNull() ?: 0
            if (phone.isEmpty() || amt <= 0) {
                Toast.makeText(this, "Isi nomor dan nominal dengan benar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Simulasi topup: langsung kembalikan bukti
            val receipt = ReceiptMaker.topupReceipt(phone, amt)
            // Untuk demo, share via chooser
            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_TEXT, receipt)
            }
            startActivity(android.content.Intent.createChooser(intent, "Share Bukti TopUp (contoh)"))
        }
    }
}
