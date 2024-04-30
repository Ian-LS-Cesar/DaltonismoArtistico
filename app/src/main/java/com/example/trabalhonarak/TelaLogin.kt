package com.example.trabalhonarak

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TelaLogin : AppCompatActivity() {

    // Credenciais de admin hardcoded para fins de demonstração
    private val adminUsername = "admin"
    private val adminPassword = "admin123"
    private lateinit var botaoLogin: Button
    private lateinit var textoLogin: EditText
    private lateinit var textoSenha: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_login)

        botaoLogin = findViewById(R.id.botaoLogin)

        botaoLogin.setOnClickListener {
            val username = textoLogin.text.toString()
            val password = textoSenha.text.toString()

            if (username == adminUsername && password == adminPassword) {
                // Se as credenciais coincidirem, exibir mensagem de sucesso
                val message = "Login bem-sucedido como administrador!"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                val message = "Cadastro incorreto!"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
