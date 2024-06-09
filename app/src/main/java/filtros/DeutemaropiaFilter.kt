package filtros

import android.graphics.Bitmap
import android.graphics.Color

class DeutemaropiaFilter {
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

            // Deutemaropia (red-green color blindness) correction
            val newR = (r * 0.625f + g * 0.375f).toInt()
            val newG = (r * 0.7f + g * 0.3f).toInt()
            val newB = b

            pixels[i] = Color.rgb(newR, newG, newB)
        }

        val filteredBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        filteredBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return filteredBitmap
    }
}