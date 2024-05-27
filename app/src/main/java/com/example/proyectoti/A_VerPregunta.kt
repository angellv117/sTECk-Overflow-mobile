package com.example.proyectoti

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoti.FramentsFeel.Adaptadores.RespuestaAdapter
import com.example.proyectoti.FramentsFeel.Adaptadores.RespuestaClass
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_aver_pregunta.*
import kotlinx.android.synthetic.main.activity_feel2.*

@Suppress("DEPRECATION")
class A_VerPregunta : AppCompatActivity() {
    var numero_control = ""
    var id_preg = ""

    private lateinit var RespuestasList:ArrayList<RespuestaClass>
    private lateinit var databse : DatabaseReference


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aver_pregunta)

        numero_control = intent.getStringExtra("nc").toString()
        id_preg = intent.getStringExtra("id").toString()

        titulo_txt.text = intent.getStringExtra("titulo")
        cuerpo_txt.text = intent.getStringExtra("cuerpo")
        fecha_txt.text = "El día: "+intent.getStringExtra("fecha").toString().subSequence(0,10)
        ncomtrol_txt.text = "Pregunta: "+numero_control

        RespuestasList = arrayListOf<RespuestaClass>()

        getRespuestas()



        responder.setOnClickListener { view ->
            val i = Intent(this, A_Responder::class.java)
            i.putExtra("id", id_preg)
            i.putExtra("pregunta",intent.getStringExtra("titulo") )
            startActivity(i)
        }
    }

    private fun getRespuestas() {
        //Obtenemos la referenia de la base de datos
        databse = FirebaseDatabase.getInstance().getReference("Respuestas")
        databse.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                RespuestasList.clear()
                if(snapshot.exists()){
                    for(feelsnap in snapshot.children){
                        //Se filtra segun el id de la pregunta
                        if (feelsnap.child("idPregunta").value.toString().lowercase() == id_preg.lowercase()){
                            val data = feelsnap.getValue(RespuestaClass::class.java)
                            RespuestasList.add(data!!)
                        }
                    }
                    val ordenada = RespuestasList.sortedByDescending{ it.fecha_resp}
                    val mAdapter = RespuestaAdapter(ordenada)
                    RVPreguntas.layoutManager = LinearLayoutManager(applicationContext)
                    RVPreguntas.adapter = mAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }

        })

    }
}
