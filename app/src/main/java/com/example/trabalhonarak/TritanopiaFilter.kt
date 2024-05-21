package com.example.trabalhonarak

import android.graphics.Bitmap

class TritanopiaFilter {
    fun aplicarFiltroTritanopia(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val tritanopiaBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        for (i in 0 until pixels.size) {
            var vermelho = (pixels[i] shr 16) and 0xFF
            val verde = (pixels[i] shr 8) and 0xFF
            var azul = pixels[i] and 0xFF
            // Diminuir a intensidade do azul e amarelo
            vermelho = (vermelho * 0.9).toInt()
            azul = (azul * 0.9).toInt()
            // Limitar os valores de cores
            vermelho = Math.min(255, vermelho)
            azul = Math.min(255, azul)
            // Atualizar pixel
            pixels[i] = (vermelho shl 16) or (verde shl 8) or azul
        }
        tritanopiaBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return tritanopiaBitmap
    }

}