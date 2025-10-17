package com.kurnia.atmmini

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReceiptMaker {
    fun sampleReceipt(): String {
        val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        return """ATM MINI KURNIA
Alamat: Jl. Contoh No.1
----------------------------
Tanggal : $now
Jenis   : Penarikan Tunai
Nominal : Rp 150.000
Saldo   : Rp 850.000
----------------------------
Terima kasih telah menggunakan ATM Mini Kurnia
""".trimIndent()
    }

    fun topupReceipt(phone: String, amount: Int): String {
        val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        return """ATM MINI KURNIA - TOP UP E-WALLET
Tanggal : $now
Tujuan  : $phone
Jumlah  : Rp ${"%,d".format(amount)}
Status  : BERHASIL
Ref     : ${System.currentTimeMillis()}
----------------------------
Terima kasih
""".trimIndent()
    }
}
