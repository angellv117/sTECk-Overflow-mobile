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
import com.example.proyectoti.FramentsFeel.Adaptadores.RespuestaClass
import com.example.proyectoti.FramentsFeel.Adaptadores.SusRespuestaAdapter
import com.example.proyectoti.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_feel.*
import kotlinx.android.synthetic.main.fragment_sus_preguntas.*
import kotlinx.android.synthetic.main.fragment_sus_respuestas.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class F_SusRespuestas : Fragment() {
    private lateinit var feelList:ArrayList<RespuestaClass>
    private lateinit var databse : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sus_respuestas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feelList = arrayListOf<RespuestaClass>()
        getRespuestas()
        configSwipe()
    }

    @SuppressLint("ResourceAsColor")
    private fun configSwipe() {
        refrescar3.setColorSchemeColors(R.color.pickColor)
        refrescar3.setOnRefreshListener {
            getRespuestas()
            refrescar3.isRefreshing = false
        }
    }

    private fun getRespuestas() {
        //Obtenemos la referenia de la base de datos
        databse = FirebaseDatabase.getInstance().getReference("Respuestas")
        databse.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                feelList.clear()
                //Recuperamos el numero de control
                val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val ncontrol = prefs!!.getString("NControl", "0")
                if(snapshot.exists()){
                    for(feelsnap in snapshot.children){
                        //Se filtra con forme el numero de control dado en la secion
                        if (feelsnap.child("nc_resp").value.toString().lowercase() == ncontrol!!.lowercase()){
                            val data = feelsnap.getValue(RespuestaClass::class.java)
                            feelList.add(data!!)
                        }
                    }
                    val ordenada = feelList.sortedByDescending{ it.fecha_resp}
                    val mAdapter = SusRespuestaAdapter(ordenada,{onItemSelected(it)}, {onPressedItem(it)})
                    RVsusRespuestas.apply {
                        layoutManager= LinearLayoutManager(activity)
                        adapter= mAdapter
                    }
                }
                else{
                    RVsusRespuestas.apply {
                        layoutManager= LinearLayoutManager(activity)
                        adapter= SusRespuestaAdapter(feelList,{onItemSelected(it)}, {onPressedItem(it)})
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun onItemSelected(feel: RespuestaClass){
        //Creando la vista emergente
        val mDialog = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val mDialogViw = inflater.inflate(R.layout.activity_asus_respuestas_ver, null)
        mDialog.setView(mDialogViw)

        //Inicando componetes
        val tituloPreg = mDialogViw.findViewById<TextView>(R.id.tituloSResp_txt)
        val cuerpoResp = mDialogViw.findViewById<EditText>(R.id.cuerpo_resp)
        val ncResp = mDialogViw.findViewById<TextView>(R.id.nc_resp)
        val fechaResp= mDialogViw.findViewById<TextView>(R.id.fecha_resp)
        val boton = mDialogViw.findViewById<TextView>(R.id.BEditar)

        //Poniendo informacion
        tituloPreg.text = feel.tituloPregunta
        cuerpoResp.setText(feel.cuerpo_resp)
        ncResp.text= feel.nc_resp
        fechaResp.text= feel.fecha_resp.toString().subSequence(0, 10)

        val alertDialog = mDialog.create()
        alertDialog.show()

        boton.setOnClickListener {
            openDialogEditar(
                feel.id.toString(),
                feel.idPregunta.toString(),
                feel.tituloPregunta.toString(),
                cuerpoResp.text.toString(),
                feel.nc_resp.toString()
            )
        }
    }

    fun onPressedItem(feel: RespuestaClass){
        val mDialog = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val mDialogViw = inflater.inflate(R.layout.aviso_delete, null)
        mDialog.setView(mDialogViw)

        val botonSi = mDialogViw.findViewById<Button>(R.id.Bsi)
        val botonno = mDialogViw.findViewById<Button>(R.id.Bno)

        val alertDialog = mDialog.create()
        alertDialog.show()

        botonSi.setOnClickListener {
            databse = FirebaseDatabase.getInstance().getReference("Respuestas").child(feel.id.toString())
            val task = databse.removeValue()
            task.addOnSuccessListener {
                Toast.makeText(activity, "Respuesta eliminada con exito", Toast.LENGTH_SHORT).show()
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
    private fun openDialogEditar(id: String,idpreg: String, titulopreg: String,cuerpo: String, nc: String) {
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

            //comparando informacion
            if( cuerpo.isNotEmpty()){
                //hacemos el update
                databse = FirebaseDatabase.getInstance().getReference("Respuestas").child(id)
                val respuestaobj = RespuestaClass(id,idpreg,titulopreg,cuerpo,newfecha,nc)
                databse.setValue(respuestaobj)
                Toast.makeText(activity, "Respuesta Editada con exito", Toast.LENGTH_SHORT).show()
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