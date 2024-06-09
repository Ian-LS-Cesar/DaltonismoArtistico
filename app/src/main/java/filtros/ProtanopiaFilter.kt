package filtros

import android.graphics.Bitmap
import android.graphics.Color

class ProtanopiaFilter {
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

            // Protanopia (red-blindness) correction
            val newR = (r * 0.567f + g * 0.433f).toInt()
            val newG = g
            val newB = b

            pixels[i] = Color.rgb(newR, newG, newB)
        }

        val filteredBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        filteredBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return filteredBitmap
    }
}