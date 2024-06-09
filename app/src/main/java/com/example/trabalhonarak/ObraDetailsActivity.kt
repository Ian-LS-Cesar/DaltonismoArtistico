package com.example.trabalhonarak

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import filtros.DeutemaropiaFilter
import filtros.ProtanopiaFilter
import filtros.TritanopiaFilter

class ObraDetailsActivity : AppCompatActivity() {
    private lateinit var voltarGaleriaBtn: ImageButton
    private lateinit var spinner: Spinner
    private lateinit var imagemObraDetails: ImageView
    private lateinit var originalBitmap: Bitmap

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
        setContentView(R.layout.activity_obra_details)
        voltarGaleriaBtn = findViewById(R.id.botaoVoltarGaleria)
        spinner = findViewById(R.id.spinner)
        imagemObraDetails = findViewById(R.id.imagemObraDetails)

        val obra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_OBRA, Obra::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_OBRA) as? Obra
        }

        voltarGaleriaBtn.setOnClickListener { voltarGaleria() }

        obra?.let {
            findViewById<TextView>(R.id.nomeObraDetails).text = it.nome
            findViewById<TextView>(R.id.autorObraDetails).text = it.autor
            findViewById<TextView>(R.id.anoObraDetails).text = it.ano

            val decodedBytes = Base64.decode(it.imagem, Base64.DEFAULT)
            originalBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            imagemObraDetails.setImageBitmap(originalBitmap)
            spinner.adapter = ArrayAdapter.createFromResource(this, R.array.filter_options, android.R.layout.simple_spinner_item)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedFilter = parent.getItemAtPosition(position).toString()
                    when (selectedFilter) {
                        "Selecione o Filtro..." -> {
                            imagemObraDetails.setImageBitmap(originalBitmap)
                        }
                        "Deutemaropia" -> {
                            val deutemaropiaFilter = DeutemaropiaFilter()
                            val originalBitmapCopy = originalBitmap.copy(originalBitmap.config, true)
                            val filteredBitmap = deutemaropiaFilter.applyFilter(originalBitmapCopy)
                            if (filteredBitmap!= null) {
                                imagemObraDetails.setImageBitmap(filteredBitmap)
                            } else {
                                Log.e("Filter", "Filtered bitmap is null")
                            }
                        }
                        "Protanopia" -> {
                            val protanopiaFilter = ProtanopiaFilter()
                            val originalBitmapCopy = originalBitmap.copy(originalBitmap.config, true)
                            val filteredBitmap = protanopiaFilter.applyFilter(originalBitmapCopy)
                            if (filteredBitmap!= null) {
                                imagemObraDetails.setImageBitmap(filteredBitmap)
                            } else {
                                Log.e("Filter", "Filtered bitmap is null")
                            }
                        }
                        "Tritanopia" -> {
                            val tritanopiaFilter = TritanopiaFilter()
                            val originalBitmapCopy = originalBitmap.copy(originalBitmap.config, true)
                            val filteredBitmap = tritanopiaFilter.applyFilter(originalBitmapCopy)
                            if (filteredBitmap!= null) {
                                imagemObraDetails.setImageBitmap(filteredBitmap)
                            } else {
                                Log.e("Filter", "Filtered bitmap is null")
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        }?: handleMissingObraData()
    }

    private fun handleMissingObraData() {
        Toast.makeText(this, "Obra data is missing", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun voltarGaleria() {
        val intent = Intent(this, TelaListarObras::class.java)
        startActivity(intent)
    }
}