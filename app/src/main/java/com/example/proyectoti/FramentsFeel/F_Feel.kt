package com.example.proyectoti.FramentsFeel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoti.A_VerPregunta
import com.example.proyectoti.FramentsFeel.Adaptadores.FeelAdapter
import com.example.proyectoti.FramentsFeel.Adaptadores.FeelClass
import com.example.proyectoti.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_feel2.*
import kotlinx.android.synthetic.main.fragment_feel.*

class F_Feel : Fragment() {
    private lateinit var feelList:ArrayList<FeelClass>
    private lateinit var databse : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feelList = arrayListOf<FeelClass>()
        getFeel()



        //Buscador de preguntas
        buscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val filtrada = feelList.filter {
                    it.filter!!.lowercase().contains(newText.toString().lowercase())
                }
                val ordenada = filtrada.sortedByDescending{ it.fecha}
                val mAdapter = FeelAdapter(ordenada, {onItemSelected(it)}) { onPressedItem(it) }
                RVFeel.apply {
                    layoutManager= LinearLayoutManager(activity)
                    adapter= mAdapter
                }

                return false
            }
        })

        //Para refrescar las preguntas
        configSwipe()
    }

    @SuppressLint("ResourceAsColor")
    private fun configSwipe() {
        refrescar.setColorSchemeColors(R.color.pickColor)
        refrescar.setOnRefreshListener {
            getFeel()
            refrescar.isRefreshing = false
        }
    }

    //Para la funcion lambda que debemos pasar al adapter
    fun onItemSelected(feel: FeelClass){
        val i = Intent(activity, A_VerPregunta::class.java)
        i.putExtra("id", feel.id)
        i.putExtra("titulo", feel.titulo)
        i.putExtra("cuerpo", feel.cuerpo)
        i.putExtra("foto", feel.foto)
        i.putExtra("fecha", feel.fecha)
        i.putExtra("nc", feel.nc)
        startActivity(i)
    }
    fun onPressedItem(feel: FeelClass){
        Toast.makeText(activity, feel.titulo, Toast.LENGTH_SHORT).show()
    }

    private fun getFeel() {
        //Obtenemos la referenia de la base de datos
        databse = FirebaseDatabase.getInstance().getReference("Preguntas")
        databse.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                feelList.clear()
                if(snapshot.exists()){
                    for(feelsnap in snapshot.children){
                        val data = feelsnap.getValue(FeelClass::class.java)
                        feelList.add(data!!)
                    }
                    val ordenada = feelList.sortedByDescending{ it.fecha}
                    val mAdapter = FeelAdapter(ordenada,{onItemSelected(it)}) { onPressedItem(it) }

                    //Iniciando recycler
                    RVFeel.apply {
                        layoutManager= LinearLayoutManager(activity)
                        adapter= mAdapter
                    }
                }
                else{
                    RVFeel.apply {
                        layoutManager= LinearLayoutManager(activity)
                        adapter= FeelAdapter(feelList,{onItemSelected(it)}) { onPressedItem(it) }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
            }

        })

    }
}