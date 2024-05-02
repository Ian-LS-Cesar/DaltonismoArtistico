package com.example.trabalhonarak

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream


private val Any.surfaceProvider: Preview.SurfaceProvider?
    get() {
        return null;
    }

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSIONS = 1001
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private lateinit var spinner: Spinner
    private lateinit var openCameraButton: Button
    private lateinit var btnTelaLogin: Button
    private var selectedFilter: String? = null

    private var cameraExecutor: ExecutorService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        spinner = findViewById(R.id.spinner)
        openCameraButton = findViewById(R.id.startCameraButton) // Renomeado para openCameraButton
        btnTelaLogin = findViewById(R.id.telaLogin)
        // Configurando o Spinner com as opções de filtro
        val filters = arrayOf("Filtro 1", "Filtro 2", "Filtro 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filters)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        openCameraButton.setOnClickListener {
            if (allPermissionsGranted()) {
                selectedFilter = spinner.selectedItem as? String
                surfaceProvider?.let { it1 -> startCamera(it1) }
            } else {
                requestCameraPermissions()
            }
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        btnTelaLogin.setOnClickListener{
            goToTelaLogin()
        }
    }

    private fun goToTelaLogin(){
        val intent = Intent(this, TelaLogin::class.java)
        startActivity(intent)
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
            e.printStackTrace()
        }
        val imageBytes: ByteArray = output.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    // Função para abrir a galeria e selecionar uma imagem

    fun openGalleryForImage(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, requestCode)
    }

    private fun addObraComImagem(nome: String, imagemBase64: String) {
        val obraData = hashMapOf(
            "nome" to nome,
            "imagem" to imagemBase64
        )

        Firebase.firestore.collection("obras")
            .add(obraData)
            .addOnSuccessListener { documentReference ->
                println("Obra adicionada com ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Erro ao adicionar a obra: $e")
            }
    }


    private fun startCamera(viewFinder: Any) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor!!, MyImageAnalyzer(selectedFilter))
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
                Toast.makeText(
                    this, "Erro ao iniciar a câmera: ${exc.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun requestCameraPermissions() {
        ActivityCompat.requestPermissions(
            this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera(viewFinder = Any())
            } else {
                Toast.makeText(
                    this,
                    "Permissões não concedidas pelo usuário.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor?.shutdown()
    }
}

class MyImageAnalyzer(private val selectedFilter: String?) : ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {
        // Aqui você pode aplicar o filtro selecionado à imagem
        // Exemplo: applyFilter(image, selectedFilter)
        image.close()
    }
}
