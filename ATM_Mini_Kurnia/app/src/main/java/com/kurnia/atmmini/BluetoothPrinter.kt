package com.kurnia.atmmini

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.OutputStream
import java.util.*

class BluetoothPrinter(private val device: BluetoothDevice) {
    private var socket: BluetoothSocket? = null
    private var out: OutputStream? = null
    private val TAG = "BluetoothPrinter"

    // Standard SPP UUID
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    fun connect(): Boolean {
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid)
            socket?.connect()
            out = socket?.outputStream
            return true
        } catch (e: IOException) {
            Log.e(TAG, "connect failed", e)
            try { socket?.close() } catch (ignored: Exception) {}
            return false
        }
    }

    fun isConnected(): Boolean {
        return socket?.isConnected == true && out != null
    }

    fun disconnect() {
        try {
            out?.close()
            socket?.close()
        } catch (e: Exception) {
            // ignore
        } finally {
            out = null
            socket = null
        }
    }

    // Simple print function: send text with line feeds and a cut/pulse command if available.
    fun printSample(text: String) {
        try {
            if (out == null) return
            // ESC/POS: initialize
            out?.write(byteArrayOf(0x1B, 0x40))
            // write text (as bytes)
            out?.write(text.toByteArray(Charsets.UTF_8))
            // feed lines
            out?.write(byteArrayOf(0x0A,0x0A,0x0A))
            // Try to send cut command (may or may not be supported)
            out?.write(byteArrayOf(0x1D, 0x56, 0x00))
            out?.flush()
        } catch (e: Exception) {
            Log.e(TAG, "print failed", e)
        }
    }
}


    fun printBitmap(bitmap: android.graphics.Bitmap) {
        try {
            if (out == null) return
            val data = PrinterUtils.bitmapToEscPos(bitmap)
            out?.write(data)
            out?.flush()
        } catch (e: Exception) {
            android.util.Log.e(TAG, "print bitmap failed", e)
        }
    }
