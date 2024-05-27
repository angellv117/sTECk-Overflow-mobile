package com.example.proyectoti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectoti.FramentsFeel.Adaptadores.FeelClass
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_asus_preguntas_ver.*
import kotlinx.android.synthetic.main.activity_aver_pregunta.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION")
class A_SusPreguntas_Ver : AppCompatActivity() {
    var numero_control = ""
    var id_preg = ""

    private lateinit var databse : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asus_preguntas_ver)

        numero_control = intent.getStringExtra("nc").toString()
        id_preg = intent.getStringExtra("id").toString()

        tituloSResp_txt.setText(intent.getStringExtra("titulo"))
        cuerpoSResp_txt.setText(intent.getStringExtra("cuerpo"))
        fechaSResp_txt.text = "El día: "+intent.getStringExtra("fecha").toString().subSequence(0,10)
        ncomtrolSResp_txt.text = "Pregunta: "+numero_control
        BEditar.setOnClickListener {
            openDialogEditar(
                intent.getStringExtra("id").toString(),
                intent.getStringExtra("nc").toString()
            )
        }
    }
    private fun openDialogEditar(id: String,nc: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogViw = inflater.inflate(R.layout.aviso_update, null)
        mDialog.setView(mDialogViw)

        val botonSi = mDialogViw.findViewById<Button>(R.id.Bsi)
        val botonno = mDialogViw.findViewById<Button>(R.id.Bno)

        val alertDialog = mDialog.create()
        alertDialog.show()

        botonSi.setOnClickListener {
            //Extrayendo nueva informacion
            val newtitulo = tituloSResp_txt.text.toString()
            val newcuerpo = cuerpoSResp_txt.text.toString()
            val newfecha = getFecha()
            val newfilter = id+" "+newtitulo+" "+newcuerpo+" "+newfecha+" "+nc

            //comparando informacion
            if(newtitulo.isNotEmpty() && newcuerpo.isNotEmpty()){
                //hacemos el update
                databse = FirebaseDatabase.getInstance().getReference("Preguntas").child(id)
                val preguntaObj = FeelClass(id,newtitulo,newcuerpo,"", newfecha,nc,newfilter)
                databse.setValue(preguntaObj)


                //mandando nueva informacion
                tituloSResp_txt.setText(newtitulo)
                cuerpoSResp_txt.setText(newcuerpo)
                fechaSResp_txt.text = "El día: "+ newfecha.subSequence(0,10)
                ncomtrolSResp_txt.text = "Pregunta: "+nc
                alertDialog.dismiss()
            }
            else{
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }


        }

        botonno.setOnClickListener {
            alertDialog.dismiss()
        }

    }
    private fun getFecha(): String {
        val horaActual = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyy  hh.mm.ss"))
        return horaActual
    }
}