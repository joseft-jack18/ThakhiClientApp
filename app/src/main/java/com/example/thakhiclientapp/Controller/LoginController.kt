package com.example.thakhiclientapp.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.thakhiclientapp.MainActivity
import com.example.thakhiclientapp.Models.ClienteModel
import com.example.thakhiclientapp.Models.ConexionModel
import com.example.thakhiclientapp.R
import kotlinx.android.synthetic.main.login_activity.*

class LoginController : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        btnIngresar.setOnClickListener {
            //cndicion para que no acepte campos vacios
            if (txtCorreo.text.toString() != "" || txtPassword.text.toString() != ""){
                var url = ConexionModel.url + "Login.php?CLIemail=" + txtCorreo.text.toString() +
                        "&CLIclave=" + txtPassword.text.toString()
                var rq = Volley.newRequestQueue(this)

                val sr = JsonArrayRequest( Request.Method.GET, url, null,
                    Response.Listener { response ->
                        ClienteModel.dniCliente = response.getJSONObject(0).getString("CLIdni")
                        ClienteModel.nombreCliente = response.getJSONObject(0).getString("CLInombre")
                        ClienteModel.apellidoCliente = response.getJSONObject(0).getString("CLIapellido")

                        Toast.makeText(this,"Bienvenido " + ClienteModel.nombreCliente, Toast.LENGTH_SHORT).show()

                        var i= Intent(this, MainActivity::class.java)
                        startActivity(i)
                    },
                    Response.ErrorListener { error ->
                        error.printStackTrace()
                        Toast.makeText(this,"Usuario y/o clave incorrecto...", Toast.LENGTH_SHORT).show()
                    }
                )
                rq.add(sr)
            } else {
                Toast.makeText(this,"Debe de llenar los campos requeridos...", Toast.LENGTH_SHORT).show()
            }
        }

        //boton del registro
        txtRegistrar.setOnClickListener{
            var i = Intent(this,RegistroController::class.java)
            startActivity(i)
        }
    }
}