package com.example.proyectoti

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoti.FramentsFeel.Adaptadores.FeelAdapter
import com.example.proyectoti.FramentsFeel.Adaptadores.FeelClass
import com.example.proyectoti.FramentsFeel.Adaptadores.RespuestaAdapter
import com.example.proyectoti.FramentsFeel.Adaptadores.RespuestaClass
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_aresponder.*
import kotlinx.android.synthetic.main.activity_aver_pregunta.*
import kotlinx.android.synthetic.main.activity_hacer_pregunta.*
import kotlinx.android.synthetic.main.fragment_feel.*
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class A_Responder : AppCompatActivity() {
    private lateinit var databse : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aresponder)


        //Recuperamos los ecxtras
        Pregunta_txt.text = intent.getStringExtra("pregunta")
        val tituloPregunta =  intent.getStringExtra("pregunta")
        val idPregunta =  intent.getStringExtra("id")

        //Recuperamos el numero de control
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val numeroControl = prefs.getString("NControl", null)


        //Insertar respuesta
        BPublicarRespuesta.setOnClickListener {
            val respuesta = etrespuesta.text.toString()
            if(respuesta.isNotEmpty()){
                //indicamos que nodo de la bse se creara y se guardaran los datos
                databse = FirebaseDatabase.getInstance().getReference("Respuestas")
                //creamos un id unico
                val idRespuesta = databse.push().key!!
                //Estraemos la fecha
                val fecha = getFecha()
                //Llamamos a la data clas para crear el onjeto
                val preguntaObj = RespuestaClass(idRespuesta,idPregunta,tituloPregunta,respuesta,fecha,numeroControl)
                databse.child(idRespuesta).setValue(preguntaObj).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this, "Ha respondido con exito", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this, "Debes de llenar todos los campos ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFecha(): String {
        val horaActual = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyy  hh.mm.ss"))
        return horaActual
    }

}