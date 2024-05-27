package com.example.proyectoti

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_olvidar_password.*

class A_OlvidarPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvidar_password)
        BOlvidarP.setOnClickListener {
            recuerarPass()
        }
    }

    fun recuerarPass() {
        if(etCorreoOlvidar.text.toString().isNotEmpty()){
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(etCorreoOlvidar.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Revise su bandeja de entrada del correo", Toast.LENGTH_SHORT).show()
                        var i = Intent(this, A_MainActivity::class.java)
                        startActivity(i)
                    }
                    else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else{
            Toast.makeText(this, "Porfavor llene todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}