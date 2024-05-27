package com.example.proyectoti.FramentsFeel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoti.FramentsFeel.Adaptadores.FeelAdapter
import com.example.proyectoti.FramentsFeel.Adaptadores.FeelClass
import com.example.proyectoti.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_asus_preguntas_ver.*
import kotlinx.android.synthetic.main.fragment_feel.*
import kotlinx.android.synthetic.main.fragment_sus_preguntas.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class F_SusPreguntas : Fragment() {

    private lateinit var feelList:ArrayList<FeelClass>
    private lateinit var databse : DatabaseReference
    private lateinit var adapter: FeelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getPreguntas()
        return inflater.inflate(R.layout.fragment_sus_preguntas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feelList = arrayListOf<FeelClass>()
        getPreguntas()
        configSwipe()
    }

    @SuppressLint("ResourceAsColor")
    private fun configSwipe() {
        refrescar2.setColorSchemeColors(R.color.pickColor)
        refrescar2.setOnRefreshListener {
            getPreguntas()
            refrescar2.isRefreshing = false
        }
    }

    private fun getPreguntas() {
        //Obtenemos la referenia de la base de datos
        databse = FirebaseDatabase.getInstance().getReference("Preguntas")
        databse.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                feelList.clear()
                //Recuperamos el numero de control
                val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val ncontrol = prefs!!.getString("NControl", null)
                if(snapshot.exists()){
                    for(feelsnap in snapshot.children){
                        //Se filtra con forme el numero de control dado en la secion
                        if (feelsnap.child("nc").value.toString().lowercase() == ncontrol!!.lowercase()){
                            val data = feelsnap.getValue(FeelClass::class.java)
                            feelList.add(data!!)
                        }

                    }
                    val ordenada = feelList.sortedByDescending{ it.fecha}
                    val mAdapter = FeelAdapter(ordenada,{onItemSelected(it)}) { onPressedItem(it) }
                    RVsusPreguntas.apply {
                        layoutManager= LinearLayoutManager(activity)
                        adapter= mAdapter
                    }
                }
                else{
                    RVsusPreguntas.apply {
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

    fun onItemSelected(feel: FeelClass){
        //Creando la vista emergente
        val mDialog = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val mDialogViw = inflater.inflate(R.layout.activity_asus_preguntas_ver, null)
        mDialog.setView(mDialogViw)

        //Inicando componetes
        val titulo = mDialogViw.findViewById<EditText>(R.id.tituloSResp_txt)
        val cuerpo = mDialogViw.findViewById<EditText>(R.id.cuerpoSResp_txt)
        val nc = mDialogViw.findViewById<TextView>(R.id.ncomtrolSResp_txt)
        val fecha= mDialogViw.findViewById<TextView>(R.id.fechaSResp_txt)
        val boton = mDialogViw.findViewById<TextView>(R.id.BEditar)

        //Poniendo informacion
        titulo.setText(feel.titulo)
        cuerpo.setText(feel.cuerpo)
        nc.text= "Pregunta: "+feel.nc
        fecha.text= feel.fecha.toString().subSequence(0, 10)

        val alertDialog = mDialog.create()
        alertDialog.show()

        boton.setOnClickListener {
            openDialogEditar(
                feel.id.toString(),
                feel.nc.toString(),
                titulo.text.toString(),
                cuerpo.text.toString()
            )
        }
    }

    fun onPressedItem(feel: FeelClass){
        val mDialog = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val mDialogViw = inflater.inflate(R.layout.aviso_delete, null)
        mDialog.setView(mDialogViw)

        val botonSi = mDialogViw.findViewById<Button>(R.id.Bsi)
        val botonno = mDialogViw.findViewById<Button>(R.id.Bno)

        val alertDialog = mDialog.create()
        alertDialog.show()

        botonSi.setOnClickListener {
            databse = FirebaseDatabase.getInstance().getReference("Preguntas").child(feel.id.toString())
            val task = databse.removeValue()
            task.addOnSuccessListener {
                Toast.makeText(activity, "Pregunta eliminada con exito", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
            }
            alertDialog.dismiss()
        }
        botonno.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    //Alerta de confirmacion Si o no de editar
    private fun openDialogEditar(id: String,nc: String, titulo: String,cuerpo: String) {
        val mDialog = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val mDialogViw = inflater.inflate(R.layout.aviso_update, null)
        mDialog.setView(mDialogViw)

        val botonSi = mDialogViw.findViewById<Button>(R.id.Bsi)
        val botonno = mDialogViw.findViewById<Button>(R.id.Bno)

        val alertDialog = mDialog.create()
        alertDialog.show()

        botonSi.setOnClickListener {
            val newfecha = getFecha()
            val newfilter = id+" "+titulo+" "+cuerpo+" "+newfecha+" "+nc

            //comparando informacion
            if(titulo.isNotEmpty() && cuerpo.isNotEmpty()){
                //hacemos el update
                databse = FirebaseDatabase.getInstance().getReference("Preguntas").child(id)
                val preguntaObj = FeelClass(id,titulo,cuerpo,"", newfecha,nc,newfilter)
                databse.setValue(preguntaObj)
                Toast.makeText(activity, "Pregunta Editada con exito", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
            else{
                Toast.makeText(activity, "Llena todos los campos", Toast.LENGTH_SHORT).show()
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