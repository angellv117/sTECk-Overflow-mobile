package com.example.proyectoti.FramentsFeel.Adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoti.R

class FeelAdapter(private var lista: List<FeelClass>,
                  private val onClickListener: (FeelClass)-> Unit,
                  private val onLongClickListener: (FeelClass)-> Unit): RecyclerView.Adapter<FeelViewH>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeelViewH {
        val layoutI = LayoutInflater.from(parent.context)
        return FeelViewH(layoutI.inflate(R.layout.item_feel,parent, false))
    }

    override fun onBindViewHolder(holder: FeelViewH, position: Int) {
        val item = lista[position]
        holder.render(item, onClickListener, onLongClickListener)

    }

    override fun getItemCount(): Int {
        return lista.size
    }


}