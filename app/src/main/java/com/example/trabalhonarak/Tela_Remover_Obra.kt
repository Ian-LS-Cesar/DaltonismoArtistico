package com.example.trabalhonarak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class TelaRemoverObras : AppCompatActivity() {

    private lateinit var voltarAdminBtn: ImageButton
    private lateinit var btnRemoverObra: Button
    private lateinit var edtNomeObra: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remover_obras)
        voltarAdminBtn = findViewById(R.id.botaoVoltarTelaAdmin)
        btnRemoverObra = findViewById(R.id.btnRemoverObra)
        edtNomeObra = findViewById(R.id.edtNomeObra)  // Adicione um EditText no layout para obter o nome da obra

        btnRemoverObra.setOnClickListener {
            val nomeObra = edtNomeObra.text.toString()
            if (nomeObra.isNotEmpty()) {
                removerObraFirestore(nomeObra)
                voltarAdmin()
            } else {
                Toast.makeText(this, "Por favor, insira o nome da obra.", Toast.LENGTH_SHORT).show()
            }
        }

        voltarAdminBtn.setOnClickListener {
            voltarAdmin()
        }
    }

    private fun voltarAdmin() {
        val intent = Intent(this, TelaAdmin::class.java)
        startActivity(intent)
    }

    private fun removerObraFirestore(nome: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("obras")
            .whereEqualTo("nome", nome)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Obra não encontrada.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                for (document in documents) {
                    db.collection("obras").document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Obra removida com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao remover a obra: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar a obra: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
