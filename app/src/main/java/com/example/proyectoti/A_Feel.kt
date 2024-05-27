package com.example.proyectoti

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.proyectoti.FramentsFeel.Adaptadores.ViewAdaptador
import com.example.proyectoti.FramentsFeel.F_Feel
import com.example.proyectoti.FramentsFeel.F_SusPreguntas
import com.example.proyectoti.FramentsFeel.F_SusRespuestas
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_feel2.*
import kotlinx.android.synthetic.main.fragment_feel.*


@Suppress("DEPRECATION")
class A_Feel : AppCompatActivity() {

    val adaptador = ViewAdaptador(supportFragmentManager)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feel2)

        //Iniciar los frgments
        iniciarFragmets()

        //Para hacer una pregunta
        fab.setOnClickListener { view ->

            val i = Intent(this, A_HacerPregunta::class.java)
            startActivity(i)
        }

        //Para mostrar el buscador
        BBuscar.setOnClickListener {
            if (buscador.visibility == View.VISIBLE){
                buscador.visibility = View.GONE
            }
            else{
                buscador.visibility = View.VISIBLE
            }
        }

        //Cerrar secion
        BCerrar_Secion.setOnClickListener {
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }


    private fun iniciarFragmets() {
        //Recuperamos el numero de control
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val ncontrol = prefs!!.getString("NControl", "0")
        Usuario_txt.text = "Usuario Actual: "+ncontrol

        adaptador.AgregarFrament(F_Feel(), "")
        adaptador.AgregarFrament(F_SusPreguntas(), "")
        adaptador.AgregarFrament(F_SusRespuestas(), "")
        viewPage.adapter = adaptador
        tabs.setupWithViewPager(viewPage)


        //Iconos
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_home)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_preguntas)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_respuestas)

    }
}