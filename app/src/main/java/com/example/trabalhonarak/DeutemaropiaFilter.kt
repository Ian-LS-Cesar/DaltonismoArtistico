package com.example.trabalhonarak

import android.graphics.Bitmap
class DeutemaropiaFilter {
    fun aplicarFiltroDeutomaropia(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val deuteranopiaBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in 0 until pixels.size) {
            var vermelho = (pixels[i] shr 16) and 0xFF
            var verde = (pixels[i] shr 8) and 0xFF
            val azul = pixels[i] and 0xFF

            // Diminuir a intensidade do vermelho e verde
            vermelho = (vermelho * 0.8).toInt()
            verde = (verde * 0.8).toInt()

            // Limitar os valores de cores
            vermelho = Math.min(255, vermelho)
            verde = Math.min(255, verde)
            // Atualizar pixel
            pixels[i] = (vermelho shl 16) or (verde shl 8) or azul
        }
        deuteranopiaBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return deuteranopiaBitmap
    }
}