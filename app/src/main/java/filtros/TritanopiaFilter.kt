package filtros

import android.graphics.Bitmap
import android.graphics.Color

class TritanopiaFilter {
    fun applyFilter(bitmap: Bitmap): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in pixels.indices) {
            val color = pixels[i]
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)

            // Tritanopia (blue-blindness) correction
            val newR = r
            val newG = (g * 0.7f + b * 0.3f).toInt()
            val newB = b

            pixels[i] = Color.rgb(newR, newG, newB)
        }

        val filteredBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        filteredBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return filteredBitmap
    }
}