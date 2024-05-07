package com.example.trabalhonarak

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class TelaAdmin : AppCompatActivity() {

    // Credenciais de admin hardcoded para fins de demonstração

    private lateinit var btnVoltarLogin: ImageButton
    private lateinit var btnInserirObraTela: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_tela)

        btnInserirObraTela = findViewById(R.id.botaoInserirObraTela)
        btnVoltarLogin = findViewById(R.id.botaoVoltarLogin)


        btnVoltarLogin.setOnClickListener{
            voltarHome()
        }

        btnInserirObraTela.setOnClickListener{
            irParaInserirObra()
        }
    }
    private fun voltarHome(){
        val intent = Intent(this, TelaLogin::class.java)
        startActivity(intent)
    }

    private fun irParaInserirObra(){
        val intent = Intent(this, TelaInserirObra::class.java)
        startActivity(intent)
    }
}