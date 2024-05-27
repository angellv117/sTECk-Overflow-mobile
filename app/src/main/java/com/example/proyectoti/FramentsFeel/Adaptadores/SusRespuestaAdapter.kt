package com.example.proyectoti.FramentsFeel.Adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoti.R

class SusRespuestaAdapter (private val lista : List<RespuestaClass>,
                           private  val onClickListener: (RespuestaClass)-> Unit,
                           private val onLongClickListener: (RespuestaClass)-> Unit): RecyclerView.Adapter<SusRespuestaViewH>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SusRespuestaViewH {
        val layoutI = LayoutInflater.from(parent.context)
        return SusRespuestaViewH(layoutI.inflate(R.layout.item_sus_respuesta,parent, false))
    }

    override fun onBindViewHolder(holder: SusRespuestaViewH, position: Int) {
        val item = lista[position]
        holder.render(item, onClickListener, onLongClickListener)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

}