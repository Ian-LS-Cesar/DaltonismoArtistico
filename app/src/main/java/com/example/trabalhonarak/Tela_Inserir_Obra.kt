package com.example.trabalhonarak

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.InputStream

class TelaInserirObra : AppCompatActivity() {

    private lateinit var btnVoltarAdmin: ImageButton
    private val btnSelecionarImagem: Button by lazy { findViewById(R.id.botaoEscolherImagem) }
    private val nomeObra: TextView by lazy { findViewById(R.id.caixaNomeObra) }
    private val nomeAutor: TextView by lazy { findViewById(R.id.caixaAutor)}
    private val anoObra: TextView by lazy { findViewById(R.id.caixaAno) }
    private val btnAdicionarObra: Button by lazy { findViewById(R.id.botaoAdicionarObra) }

    private var selecionarImagemUri: Uri? = null

    private val REQUEST_CODE_IMAGE_PICK = 1

    private val startForResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            selecionarImagemUri = result.data?.data
            nomeObra.text = selecionarImagemUri?.lastPathSegment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_inserir_obra)

        btnVoltarAdmin = findViewById(R.id.botaoVoltarTelaAdmin)

        btnVoltarAdmin.setOnClickListener {
            voltarAdmin()
        }

        btnSelecionarImagem.setOnClickListener {
            abrirGaleria()
            val message = "Selecionando imagem...!"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        btnAdicionarObra.setOnClickListener {
            adicionarObraFirestore()
            val message = "Obra adicionada!"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            voltarAdmin()
        }


    }

    private fun voltarAdmin(){
        val intent = Intent(this, TelaAdmin::class.java)
        startActivity(intent)
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startForResult.launch(intent)
    }

    private fun adicionarObraFirestore() {
        val nome = nomeObra.text.toString()
        val autor = nomeAutor.text.toString()
        val ano = anoObra.text.toString()
        val imageBase64 = imageToBase64(this, selecionarImagemUri!!)
        adicionarObraComImagem(nome, autor, ano, imageBase64)
    }

    fun imageToBase64(context: Context, imageUri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val buffer = ByteArray(8192)
        var bytesRead: Int
        val output = ByteArrayOutputStream()
        try {
            while (inputStream?.read(buffer).also { bytesRead = it ?: 0 } != -1) {
                output.write(buffer, 0, bytesRead)
            }
        } catch (e: Exception) {
            Log.e("Tela Inserir Obra", "Erro convertendo imagem para Base64", e)
        }
        val imageBytes: ByteArray = output.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun adicionarObraComImagem(nome: String, autor: String, ano: String, imageToBase64: String) {
        val obraData = hashMapOf(
            "imagem" to imageToBase64,
            "ano" to ano,
            "autor" to autor,
            "nome" to nome
        )

        FirebaseFirestore.getInstance().collection("obras")
            .add(obraData)
            .addOnSuccessListener { documentReference ->
                Log.d("Tela Inserir Obra", "Obra adicionada com ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Tela Inserir Obra", "Erro Adicionando Obra", e)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK) {
            selecionarImagemUri = data?.data
            nomeObra.text = selecionarImagemUri?.lastPathSegment
        }
    }
}