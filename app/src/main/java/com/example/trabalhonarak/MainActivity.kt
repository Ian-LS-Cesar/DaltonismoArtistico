package com.example.trabalhonarak

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSIONS = 1001
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private lateinit var spinner: Spinner
    private lateinit var startCameraButton: Button
    private var selectedFilter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinner)
        startCameraButton = findViewById(R.id.startCameraButton)

        // Configurando o Spinner com as opções de filtro
        val filters = arrayOf("Filtro 1", "Filtro 2", "Filtro 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filters)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        startCameraButton.setOnClickListener {
            if (allPermissionsGranted()) {
                selectedFilter = spinner.selectedItem as? String
                startCamera()
            } else {
                requestCameraPermissions()
            }
        }
    }

    private fun startCamera() {
        // Lógica para iniciar a câmera aqui
        // Aqui você pode usar o filtro selecionado (selectedFilter) para aplicar o filtro desejado
        Toast.makeText(this, "Câmera iniciada com filtro: $selectedFilter", Toast.LENGTH_SHORT)
            .show()
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
                startCamera()
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
}