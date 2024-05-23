package com.example.trabalhonarak

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TelaListarObras : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var obrasAdapter: ObrasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_listar_obras)

        recyclerView = findViewById(R.id.recyclerViewObras)
        recyclerView.layoutManager = LinearLayoutManager(this)

        obrasAdapter = ObrasAdapter(this)
        recyclerView.adapter = obrasAdapter

        // Fetch obras from Firestore
        fetchObrasFromFirestore()
    }

    private fun fetchObrasFromFirestore() {
        val db = Firebase.firestore
        val obrasCollection = db.collection("obras")

        obrasCollection.get()
            .addOnSuccessListener { result ->
                val obras = mutableListOf<Obra>()
                for (document in result) {
                    val obra= Obra(
                    document.get("nome").toString(),
                    document.get("autor").toString(),
                        document.get("ano").toString(),
                        document.get("imagem").toString())
                    obras.add(obra)
                }
                obrasAdapter.setObras(obras)
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }
}

data class Obra(
    val nome: String,
    val autor: String,
    val ano: String,
    val imagem: String
)

class ObrasAdapter(private val context: Context) : RecyclerView.Adapter<ObrasAdapter.ObraViewHolder>() {

    private var obras = mutableListOf<Obra>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObraViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_obra, parent, false)
        return ObraViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObraViewHolder, position: Int) {
        val obra = obras[position]
        holder.bind(obra)
    }

    override fun getItemCount(): Int = obras.size

    fun setObras(obras: List<Obra>) {
        this.obras = obras.toMutableList()
        notifyDataSetChanged()
    }

    inner class ObraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nomeTextView: TextView = itemView.findViewById(R.id.nomeObra)
        private val autorTextView: TextView = itemView.findViewById(R.id.autorObra)
        private val anoTextView: TextView = itemView.findViewById(R.id.anoObra)
        private val imagemImageView: ImageView = itemView.findViewById(R.id.imagemObra)

        fun bind(obra: Obra) {
            nomeTextView.text = obra.nome
            autorTextView.text = obra.autor
            anoTextView.text = obra.ano

            // Decode base64 image and set it to the ImageView
            val decodedBytes = Base64.decode(obra.imagem, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            imagemImageView.setImageBitmap(bitmap)
        }
    }
}