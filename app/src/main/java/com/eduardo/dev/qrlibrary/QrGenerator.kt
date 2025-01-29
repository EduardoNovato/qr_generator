package com.eduardo.dev.qrlibrary

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.provider.MediaStore
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

class QrGenerator {

    fun generateQr(
        content: String,
        width: Int = 512,
        height: Int = 512,
        qrColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE,
        logo: Bitmap? = null
    ): Bitmap {
        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H
        )

        val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)

        val qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                qrBitmap.setPixel(x, y, if (bitMatrix[x, y]) qrColor else backgroundColor)
            }
        }

        // Si hay logo, lo superponemos en el centro
        return if (logo != null) overlayLogo(qrBitmap, logo) else qrBitmap
    }

    private fun overlayLogo(qrBitmap: Bitmap, logo: Bitmap): Bitmap {
        val canvas = Canvas(qrBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Reducir el tamaño del logo
        val logoSize = (qrBitmap.width * 0.15).toInt()
        val scaledLogo = Bitmap.createScaledBitmap(logo, logoSize, logoSize, true)

        // Crear un fondo blanco más grande que el logo
        val backgroundSize = (logoSize * 1.2).toInt()
        val background = Bitmap.createBitmap(backgroundSize, backgroundSize, Bitmap.Config.ARGB_8888)
        val bgCanvas = Canvas(background)
        bgCanvas.drawColor(Color.WHITE)
        bgCanvas.drawBitmap(scaledLogo, ((backgroundSize - logoSize) / 2).toFloat(), ((backgroundSize - logoSize) / 2).toFloat(), null)

        // Calcular posición centrada
        val centerX = (qrBitmap.width - background.width) / 2
        val centerY = (qrBitmap.height - background.height) / 2

        // Dibujar fondo blanco y luego logo
        canvas.drawBitmap(background, centerX.toFloat(), centerY.toFloat(), paint)

        return qrBitmap
    }

    fun saveQrToGallery(context: Context, bitmap: Bitmap, fileName: String = "QRCode.png"): Boolean {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/QR Codes")
            }

            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: return false

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false // Error al guardar
        }
    }


}
