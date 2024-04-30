package com.example.trabalhonarak

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private val Any.surfaceProvider: Preview.SurfaceProvider?
    get() {
        return null;
    }

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSIONS = 1001
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private lateinit var spinner: Spinner
    private lateinit var openCameraButton: Button
    private var selectedFilter: String? = null

    private var cameraExecutor: ExecutorService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinner)
        openCameraButton = findViewById(R.id.startCameraButton) // Renomeado para openCameraButton

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
