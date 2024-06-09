package com.example.trabalhonarak

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Preview
import java.util.concurrent.ExecutorService


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
    private lateinit var btnGaleria: Button
    private var selectedFilter: String? = null


    private var cameraExecutor: ExecutorService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTelaLogin = findViewById(R.id.telaLogin)
        btnGaleria = findViewById(R.id.botaoGaleria)
        // Configurando o Spinner com as opções de filtro


        btnTelaLogin.setOnClickListener{
            goToTelaLogin()
        }
        btnGaleria.setOnClickListener{
            irParaGaleria()
        }
    }

    private fun goToTelaLogin(){
        val intent = Intent(this, TelaLogin::class.java)
        startActivity(intent)
    }

    private fun irParaGaleria(){
        val intent = Intent(this, TelaListarObras::class.java)
        startActivity(intent)
    }
}

