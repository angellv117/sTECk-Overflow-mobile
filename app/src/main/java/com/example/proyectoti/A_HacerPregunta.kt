package com.example.proyectoti

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectoti.FramentsFeel.Adaptadores.FeelClass
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_hacer_pregunta.*
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION")
class A_HacerPregunta : AppCompatActivity() {
    private lateinit var databse : DatabaseReference
    val foto = "https://www.google.com/search?q=itsjr%20logo&tbm=isch&hl=es&sa=X&ved=0CBwQtI8BKABqFwoTCKjxoqXOlfwCFQAAAAAdAAAAABAH&biw=1343&bih=657#imgrc=xKybKTZWZtoOCM\""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hacer_pregunta)
        //Recuperamos el numero de control
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val ncontrol = prefs.getString("NControl", null)
        //Toast.makeText(this, ncontrol, Toast.LENGTH_SHORT).show()

        //Publicar pregunta
        BPublicar.setOnClickListener {
            val titulo = ettitulo.text.toString()
            val cuerpo = etcuerpo.text.toString()
            if(titulo.isNotEmpty() && cuerpo.isNotEmpty()){
                //indicamos que nodo de la bse se creara y se guardaran los datos
                databse = FirebaseDatabase.getInstance().getReference("Preguntas")
                //creamos un id unico
                val idTitulo = databse.push().key!!
                //Estraemos la fecha
                val fecha = getFecha()
                //Llamamos a la data clas para crear el onjeto
                val filtro = titulo+" "+cuerpo+" "+fecha+" "+ncontrol.toString()
                val preguntaObj = FeelClass(idTitulo,titulo,cuerpo,foto, fecha,ncontrol.toString(),filtro)
                databse.child(idTitulo).setValue(preguntaObj).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this, "Pregunta agregada con exito", Toast.LENGTH_SHORT).show()
                        onBackPressed()
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