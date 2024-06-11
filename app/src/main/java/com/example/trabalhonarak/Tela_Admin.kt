package com.example.trabalhonarak

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class TelaAdmin : AppCompatActivity() {

    // Credenciais de admin hardcoded para fins de demonstração

    private lateinit var btnVoltarLogin: ImageButton
    private lateinit var btnInserirObraTela: Button
    private lateinit var btnAlterarObras: Button
    private lateinit var btnRemoverObras: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_tela)

        btnInserirObraTela = findViewById(R.id.botaoInserirObraTela)
        btnVoltarLogin = findViewById(R.id.botaoVoltarLogin)
        btnAlterarObras = findViewById(R.id.botaoAlterarObras)
        btnRemoverObras = findViewById(R.id.botaoRemoverObras)

        btnRemoverObras.setOnClickListener{
            irParaRemocao()
        }

        btnVoltarLogin.setOnClickListener{
            voltarHome()
        }

        btnInserirObraTela.setOnClickListener{
            irParaInserirObra()
        }


        btnAlterarObras.setOnClickListener{
            irParaAlterar()
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

    private fun irParaGaleria(){
        val intent = Intent(this, TelaListarObras::class.java)
        startActivity(intent)
    }

    private fun irParaAlterar(){
        val intent = Intent(this, TelaAlterarObras::class.java)
        startActivity(intent)
    }
    private fun irParaRemocao(){
        val intent = Intent(this, TelaRemoverObras::class.java)
        startActivity(intent)
    }
}