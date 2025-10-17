package com.kurnia.atmmini

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

object PrinterUtils {
    // Convert bitmap to ESC/POS raster bytes (simple 1-bit conversion)
    fun bitmapToEscPos(bitmap: Bitmap): ByteArray {
        val width = bitmap.width
        val height = bitmap.height
        val baos = ByteArrayOutputStream()
        // Initialize
        baos.write(byteArrayOf(0x1B, 0x40))
        // Set line spacing to 24 dots
        baos.write(byteArrayOf(0x1B, 0x33, 24))
        // naive conversion: send as PNG (some newer printers accept image printing via proprietary commands)
        // For better compatibility, implement real raster conversion per printer spec.
        val png = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, png)
        baos.write(png.toByteArray())
        // feed + cut
        baos.write(byteArrayOf(0x0A,0x0A,0x0A, 0x1D,0x56,0x00))
        return baos.toByteArray()
    }
}
