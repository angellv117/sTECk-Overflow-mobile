package com.example.proyectoti.FramentsFeel.Adaptadores

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoti.R

class  SusRespuestaViewH (view : View): RecyclerView.ViewHolder(view){
    val titulo = view.findViewById<TextView>(R.id.titulo_sresp)
    val cuerpo = view.findViewById<TextView>(R.id.cuerpo_sresp)
    val fecha = view.findViewById<TextView>(R.id.fecha_sresp)
    val nc = view.findViewById<TextView>(R.id.nc_sresp)
    fun render(respuesta: RespuestaClass,onClickListener: (RespuestaClass)-> Unit, onLongClickListener: (RespuestaClass)-> Unit){
        titulo.text = "Pregunta: "+respuesta.tituloPregunta
        cuerpo.text= "Respondite: "+respuesta.cuerpo_resp
        fecha.text= respuesta.fecha_resp
        nc.text= respuesta.nc_resp

        //Funcion del onclick Real
        itemView.setOnClickListener {
            onClickListener(respuesta)
        }
        //Funcion precioanando
        itemView.setOnLongClickListener {
            onLongClickListener(respuesta)
            return@setOnLongClickListener true
        }
    }
}