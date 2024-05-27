package com.example.proyectoti.FramentsFeel.Adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoti.R

class RespuestaAdapter (private val lista : List<RespuestaClass>): RecyclerView.Adapter<RespuestaViewH>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RespuestaViewH {
        val layoutI = LayoutInflater.from(parent.context)
        return RespuestaViewH(layoutI.inflate(R.layout.item_respuesta,parent, false))
    }

    override fun onBindViewHolder(holder: RespuestaViewH, position: Int) {
        val item = lista[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

}