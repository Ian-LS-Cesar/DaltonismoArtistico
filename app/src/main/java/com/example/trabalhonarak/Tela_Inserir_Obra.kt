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

    private lateinit var voltarAdminBtn: ImageButton
    private lateinit var selectImageBtn: Button
    private lateinit var obraNameTextView: TextView
    private lateinit var autorNameTextView: TextView
    private lateinit var anoObraTextView: TextView
    private lateinit var addObraBtn: Button

    private var selectedImageUri: Uri? = null

    private val REQUEST_CODE_IMAGE_PICK = 1
    private val BUFFER_SIZE = 8192

    private val startForResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedImageUri = result.data?.data
            convertImageToBase64(this, selectedImageUri!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_inserir_obra)

        initViews()
        setListeners()
    }

    private fun initViews() {
        voltarAdminBtn = findViewById(R.id.botaoVoltarTelaAdmin)
        selectImageBtn = findViewById(R.id.botaoEscolherImagem)
        obraNameTextView = findViewById(R.id.caixaNomeObra)
        autorNameTextView = findViewById(R.id.caixaAutor)
        anoObraTextView = findViewById(R.id.caixaAno)
        addObraBtn = findViewById(R.id.botaoAdicionarObra)
    }

    private fun setListeners() {
        voltarAdminBtn.setOnClickListener { voltarAdmin() }
        selectImageBtn.setOnClickListener { abrirGaleria() }
        addObraBtn.setOnClickListener {
            adicionarObraFirestore()
            voltarAdmin()
        }

    }

    private fun voltarAdmin() {
        val intent = Intent(this, TelaAdmin::class.java)
        startActivity(intent)
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startForResult.launch(intent)
    }

    private fun adicionarObraFirestore() {
        val obraName = obraNameTextView.text.toString()
        val autorName = autorNameTextView.text.toString()
        val ano = anoObraTextView.text.toString()
        val imageBase64 = convertImageToBase64(this, selectedImageUri!!)
        addObraToFirestore(obraName, autorName, ano, imageBase64)
    }

    private fun convertImageToBase64(context: Context, imageUri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val buffer = ByteArray(BUFFER_SIZE)
        var bytesRead: Int
        val output = ByteArrayOutputStream()

        try {
            while (inputStream?.read(buffer).also { bytesRead = it?: 0 }!= -1) {
                output.write(buffer, 0, bytesRead)
            }
        } catch (e: Exception) {
            Log.e("Tela Inserir Obra", "Erro convertendo imagem para Base64", e)
        }

        val imageBytes: ByteArray = output.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun addObraToFirestore(nome: String, autor: String, ano: String, imageBase64: String) {
        val obraData = hashMapOf(
            "imagem" to imageBase64,
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
}