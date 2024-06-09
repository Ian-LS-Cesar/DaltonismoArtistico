package com.example.trabalhonarak

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TelaListarObras : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var obrasAdapter: ObrasAdapter
    private lateinit var btnVoltarHome: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_listar_obras)

        btnVoltarHome = findViewById(R.id.botaoVoltarHome)
        recyclerView = findViewById(R.id.recyclerViewObras)
        recyclerView.layoutManager = LinearLayoutManager(this)

        obrasAdapter = ObrasAdapter(this)
        recyclerView.adapter = obrasAdapter

        // Fetch obras from Firestore
        fetchObrasFromFirestore()

        btnVoltarHome.setOnClickListener{
            voltarHome()
        }
    }

    private fun fetchObrasFromFirestore() {
        val db = Firebase.firestore
        val obrasCollection = db.collection("obras")

        obrasCollection.get()
            .addOnSuccessListener { result ->
                val obras = mutableListOf<Obra>()
                for (document in result) {
                    val obra = Obra(
                        document.get("nome").toString(),
                        document.get("autor").toString(),
                        document.get("ano").toString(),
                        document.get("imagem").toString()
                    )
                    obras.add(obra)
                }
                obrasAdapter.setObras(obras)
            }
            .addOnFailureListener { exception ->
                println("Error fetching obras: ${exception.message}")
            }
    }
    private fun voltarHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

data class Obra(
    val nome: String,
    val autor: String,
    val ano: String,
    val imagem: String
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeString(autor)
        parcel.writeString(ano)
        parcel.writeString(imagem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Obra> {
        override fun createFromParcel(parcel: Parcel): Obra {
            return Obra(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!
            )
        }

        override fun newArray(size: Int): Array<Obra?> {
            return arrayOfNulls(size)
        }
    }
}

class ObrasAdapter(private val context: Context) : RecyclerView.Adapter<ObrasAdapter.ObraViewHolder>() {

    private var obrasList = mutableListOf<Obra>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObraViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_obra, parent, false)
        return ObraViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObraViewHolder, position: Int) {
        val obra = obrasList[position]
        holder.bind(obra)

        holder.itemView.setOnClickListener {
            ObraDetailsActivity.startActivity(context, obra)
        }
    }

    override fun getItemCount(): Int {
        return obrasList.size
    }

    fun setObras(obras: List<Obra>) {
        this.obrasList = obras.toMutableList()
        notifyDataSetChanged()
    }

    class ObraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nomeObraTextView: TextView = itemView.findViewById(R.id.nomeObra)
        private val autorObraTextView: TextView = itemView.findViewById(R.id.autorObra)
        private val anoObraTextView: TextView = itemView.findViewById(R.id.anoObra)
        private val imagemObraImageView: ImageView = itemView.findViewById(R.id.imagemObra)

        fun bind(obra: Obra) {
            nomeObraTextView.text = obra.nome
            autorObraTextView.text = obra.autor
            anoObraTextView.text = obra.ano

            val decodedBytes = Base64.decode(obra.imagem, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            imagemObraImageView.setImageBitmap(bitmap)
        }
    }
}
