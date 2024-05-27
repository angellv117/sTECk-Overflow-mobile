package com.example.proyectoti

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("DEPRECATION")
class A_MainActivity : AppCompatActivity() {
    private val GOOGLE_SIN_IN =100
    private val callbackManager = CallbackManager.Factory.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session()
        //para mostrar el menu de registrar
        Registrar.setOnClickListener{
            etCorreoSin.setText("")
            etContraseñasin01.setText("")
            etContraseñasin02.setText("")
            mostrarRegistrar()
        }
        //para mostrar el menu de Login
        login.setOnClickListener{
            etCorreo.setText("")
            etContraseña.setText("")
            mostrarLogin()
        }

        //Boton de registrar
        BRegistrar.setOnClickListener{
            var correoreRistrar = etCorreoSin.text.toString()
            var contraseña1Registrar = etContraseñasin01.text.toString()
            var contraseña2Registrar = etContraseñasin02.text.toString()
            registrar(correoreRistrar, contraseña1Registrar, contraseña2Registrar)
        }

        //Boton de Login
        BLogin.setOnClickListener {

            var  correoLog = etCorreo.text.toString()
            var contraseñaLog = etContraseña.text.toString()
            autenticar(correoLog, contraseñaLog)
        }

        // Login con Google
        BGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString((R.string.default_web_client_id)))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIN_IN)
        }

        //LOGIN POR FACEBOOK
        BFace.setOnClickListener{
            LoginManager.getInstance().logOut()
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(callbackManager,object: FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult) {
                    result?.let {
                        val token = it.accessToken
                        val credencial = FacebookAuthProvider.getCredential(token.token)
                        FirebaseAuth.getInstance()
                            .signInWithCredential(credencial).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(applicationContext, "Bienvenido", Toast.LENGTH_SHORT).show()
                                    val ncontrol = it.result?.user?.displayName ?:"".toString().substring(0,9)
                                    guardarLogIn(ncontrol)
                                    var i = Intent(applicationContext, A_Feel::class.java)
                                    startActivity(i)
                                }
                                else{
                                    Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                    showAlert("Error", error.toString())
                }

            })
        }

        
        //olvidar contraseña
        OlvidarPassword.setOnClickListener {
            var i = Intent(this, A_OlvidarPassword::class.java)
            startActivity(i)
        }


    }
    //Verifica si hay secion activa
    private fun session() {
        //Guardar datos del usuario
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val nControl = prefs.getString("NControl", null)
        if (nControl!= null){
            var i = Intent(this, A_Feel::class.java)
            startActivity(i)
        }
    }

    //Login con correo y contraseña normales
    fun autenticar(correoLog : String, contraseñaLog: String) {
        if(correoLog.isNotEmpty() && contraseñaLog.isNotEmpty()){
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(correoLog,contraseñaLog)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                        guardarLogIn(correoLog.subSequence(0,9).toString())
                        var i = Intent(this, A_Feel::class.java)
                        startActivity(i)
                    }
                    else{
                        showAlert("Error", "Nombre o contraseña incorrectos")
                    }
                }
        }
        else{
            Toast.makeText(this, "Porfavor llene todos los campos", Toast.LENGTH_SHORT).show()
        }

    }

    //Funcion para mostrar menu Login
    fun mostrarLogin() {
        Registrar.background = null
        Registrar.setTextColor(resources.getColor(R.color.pickColor,null))
        login.background=resources.getDrawable(R.drawable.switch_trcks, null)
        singuplayout.visibility= View.GONE
        loginlayout.visibility = View.VISIBLE
        login.setTextColor(resources.getColor((R.color.textColor), null))
    }

    //Funcion para mostrar menu registrar
    fun mostrarRegistrar() {
        Registrar.background = resources.getDrawable(R.drawable.switch_trcks, null)
        Registrar.setTextColor(resources.getColor(R.color.textColor,null))
        login.background= null
        singuplayout.visibility= View.VISIBLE
        loginlayout.visibility = View.GONE
        login.setTextColor(resources.getColor((R.color.pickColor), null))
    }

    //Funcion para registrar
    fun registrar (correoreRistrar : String, contraseña1Registrar : String, contraseña2Registrar : String){
        //Validamos campos
        if (correoreRistrar.isNotEmpty() && contraseña1Registrar.isNotEmpty() && contraseña2Registrar.isNotEmpty()){
            //validamos conincidencias de las contraseñas
            if (contraseña1Registrar.equals(contraseña2Registrar)){
                //Validamos longitud de contraseña
                if (contraseña1Registrar.length >=7){
                    //Registrsmos usuario
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(correoreRistrar,etContraseñasin01.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(this, "Registrado con exito", Toast.LENGTH_SHORT).show()
                                mostrarLogin()
                            }
                            else{
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else{
                    Toast.makeText(this, "Las contraseñas debe ser mas de 6 caracteres", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Porfavor llene todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    //Funcion que muestra un error
    fun showAlert(titulo: String, mensaje: String ){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Acpectar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Login con Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuanta = task.getResult(ApiException::class.java)

                if ( cuanta != null ){

                    val credencial = GoogleAuthProvider.getCredential(cuanta.idToken, null)
                    FirebaseAuth.getInstance()
                        .signInWithCredential(credencial).addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                                val ncontrol = cuanta.email.toString().substring(0, 9)
                                guardarLogIn(ncontrol)
                                var i = Intent(this, A_Feel::class.java)
                                startActivity(i)
                            }
                            else{

                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            }catch (e: ApiException){
                showAlert("Error","Algo ocuarrio mal :(  \n"+e.toString())
            }

        }
    }

    private fun guardarLogIn(nControl : String){
        //Guardar datos del usuario
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("NControl", nControl)
        prefs.apply()
    }

}
