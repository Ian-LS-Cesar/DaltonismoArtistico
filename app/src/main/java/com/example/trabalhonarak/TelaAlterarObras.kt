package com.example.trabalhonarak

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class TelaAlterarObras : AppCompatActivity() {

    private lateinit var btnAlterarObra: Button
    private lateinit var edtNomeObra: EditText
    private lateinit var edtNovoNomeObra: EditText
    private lateinit var edtArtistaObra: EditText
    private lateinit var edtAnoObra: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alterar_obras)

        btnAlterarObra = findViewById(R.id.btnAlterarObra)
        edtNomeObra = findViewById(R.id.edtNomeObra)
        edtNovoNomeObra = findViewById(R.id.edtNovoNomeObra)
        edtArtistaObra = findViewById(R.id.edtArtistaObra)
        edtAnoObra = findViewById(R.id.edtAnoObra)

        btnAlterarObra.setOnClickListener {
            val nomeObra = edtNomeObra.text.toString()
            val novoNomeObra = edtNovoNomeObra.text.toString()
            val artistaObra = edtArtistaObra.text.toString()
            val anoObra = edtAnoObra.text.toString()
            if (nomeObra.isNotEmpty() && novoNomeObra.isNotEmpty()  && artistaObra.isNotEmpty() && anoObra.isNotEmpty()) {
                alterarObraFirestore(nomeObra, novoNomeObra, artistaObra, anoObra)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun alterarObraFirestore(nomeObra: String, novoNomeObra: String, artistaObra: String, anoObra: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("obras")
            .whereEqualTo("nome", nomeObra)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Obra não encontrada.", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val updates = hashMapOf<String, Any>(
                            "nome" to novoNomeObra,
                            "artista" to artistaObra,
                            "ano" to anoObra
                        )

                        db.collection("obras").document(document.id)
                            .update(updates)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Obra alterada com sucesso.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Erro ao alterar a obra.", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar a obra.", Toast.LENGTH_SHORT).show()
            }
    }
}

