package com.example.trabalhonarak

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class TelaAdmin : AppCompatActivity() {

    // Credenciais de admin hardcoded para fins de demonstração

    private lateinit var btnVoltarLogin: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_tela)

        btnVoltarLogin = findViewById(R.id.botaoVoltarLogin)


        btnVoltarLogin.setOnClickListener{
            voltarHome()
        }
    }
    private fun voltarHome(){
        val intent = Intent(this, TelaLogin::class.java)
        startActivity(intent)
    }
}