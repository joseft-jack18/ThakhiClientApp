package com.example.thakhiclientapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.thakhiclientapp.Controller.SplashController
import com.example.thakhiclientapp.Models.ClienteModel
import com.example.thakhiclientapp.Models.ConexionModel
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val fechaActual = sdf.format(Date())

        //mostrar DNI del cliente
        txtdni.text = "DNI N° " + ClienteModel.dniCliente
        //Mostrar nombres completos del cliente
        txtnombre.text = ClienteModel.nombreCliente + " " + ClienteModel.apellidoCliente


        //boton de ubicacion del cliente
        ubicacion.setOnClickListener {
            var i= Intent(this,Maps2Activity::class.java)
            startActivity(i)
        }

        /*valoracion.setOnClickListener {
            var url= ConexionModel.url + "BuscarCalificacion.php?CALfecha=" + fechaactual.toString()
            var rq= Volley.newRequestQueue(this)

            var sr= StringRequest(
                Request.Method.GET,url,
                Response.Listener { response ->
                    if(response=="0"){
                        var i= Intent(this,NotificacionActivity::class.java)
                        startActivity(i)
                    }
                    if(response=="1"){
                        ObtenerDatosEntrega(ClsConexion.dni,fechaactual)
                        var i= Intent(this,Splash2Activity::class.java)
                        startActivity(i)
                    }
                },
                Response.ErrorListener {  })
            rq.add(sr)
        }

        perfil.setOnClickListener {
            var i= Intent(this,PerfilActivity::class.java)
            startActivity(i)
        }*/

        salir.setOnClickListener {
            Toast.makeText(this,"Cerrando Sesión...", Toast.LENGTH_LONG).show()

            //Se limpian los datos del cliente
            ClienteModel.dniCliente = ""
            ClienteModel.nombreCliente = ""
            ClienteModel.apellidoCliente = ""

            //se abre la ventana de carga para mostrar el login
            var i= Intent(this,SplashController::class.java)
            startActivity(i)
        }
    }
}
