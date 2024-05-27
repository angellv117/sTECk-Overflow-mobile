package com.example.proyectoti.FramentsFeel.Adaptadores

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoti.R

class  RespuestaViewH (view : View): RecyclerView.ViewHolder(view){
    val cuerpo = view.findViewById<TextView>(R.id.cuerpo_resp)
    val fecha = view.findViewById<TextView>(R.id.fecha_resp)
    val nc = view.findViewById<TextView>(R.id.nc_resp)
    fun render(respuesta: RespuestaClass){
        cuerpo.text= respuesta.cuerpo_resp
        fecha.text= respuesta.fecha_resp
        nc.text= respuesta.nc_resp
    }
}