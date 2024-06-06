package com.example.trabalhonarak // Update with your package name

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class ObraDetailsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_OBRA = "extra_obra"

        fun startActivity(context: Context, obra: Obra) {
            val intent = Intent(context, ObraDetailsActivity::class.java)
            intent.putExtra(EXTRA_OBRA, obra)
            context.startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obra_details) // Make sure this is your layout file

        val obra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_OBRA, Obra::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_OBRA) as? Obra
        }

        obra?.let {
            findViewById<TextView>(R.id.nomeObraDetails).text = it.nome
            findViewById<TextView>(R.id.autorObraDetails).text = it.autor
            findViewById<TextView>(R.id.anoObraDetails).text = it.ano

            val decodedBytes = Base64.decode(it.imagem, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            findViewById<ImageView>(R.id.imagemObraDetails).setImageBitmap(bitmap)
        } ?: handleMissingObraData()
    }

    private fun handleMissingObraData() {
        Toast.makeText(this, "Obra data is missing", Toast.LENGTH_SHORT).show()
        finish()
    }
}