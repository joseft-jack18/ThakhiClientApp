package com.example.thakhiclientapp.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.thakhiclientapp.Models.ConexionModel
import com.example.thakhiclientapp.Models.ClienteModel
import com.example.thakhiclientapp.R
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.menu_activity.*

class LoginController : AppCompatActivity() {

    val list=ArrayList<ClienteModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        btnIngresar.setOnClickListener {

            var url= ConexionModel.url + "Login.php?CLIemail=" + txtCorreo.text.toString() + "&CLIclave=" + txtPassword.text.toString()
            var rq= Volley.newRequestQueue(this)

            val sr = JsonArrayRequest( Request.Method.GET, url, null,
                Response.Listener { response ->
                    Toast.makeText(this,"Bienvenido " + response.getJSONObject(0).getString("CLInombre"),
                        Toast.LENGTH_SHORT).show()
                    ConexionModel.dniCliente = response.getJSONObject(0).getString("CLIdni")
                    ConexionModel.nombreCliente = response.getJSONObject(0).getString("CLInombre") + " " +
                            response.getJSONObject(0).getString("CLIapellido")

                    var i= Intent(this,MenuController::class.java)
                    startActivity(i)
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Toast.makeText(this,"Usuario y/o clave incorrecto...", Toast.LENGTH_SHORT).show()
                }
            )
            rq.add(sr)
        }

        txtRegistrar.setOnClickListener{
            var i = Intent(this,RegistroController::class.java)
            startActivity(i)
        }
    }
}
