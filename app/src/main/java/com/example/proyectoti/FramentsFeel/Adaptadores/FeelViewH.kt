package com.example.proyectoti.FramentsFeel.Adaptadores

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoti.R

class  FeelViewH (view : View): RecyclerView.ViewHolder(view){
    val titulo = view.findViewById<TextView>(R.id.titulo_feel)
    val cuerpo = view.findViewById<TextView>(R.id.cuerpo_feel)
    val foto = view.findViewById<ImageView>(R.id.foto_feel)
    val fecha = view.findViewById<TextView>(R.id.fecha_feel)
    val nc = view.findViewById<TextView>(R.id.nc_feel)
    fun render(feel: FeelClass, onClickListener: (FeelClass)-> Unit, onLongClickListener: (FeelClass)-> Unit){
        titulo.text= feel.titulo
        cuerpo.text= feel.cuerpo
        fecha.text= feel.fecha
        nc.text= feel.nc

        //Funcion del onclick Real
        itemView.setOnClickListener {
                onClickListener(feel)
            }
        //Funcion precioanando
        itemView.setOnLongClickListener {
            onLongClickListener(feel)
            return@setOnLongClickListener true
        }
    }
}