package com.example.thakhiclientapp.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.example.thakhiclientapp.Models.ConexionModel
import com.example.thakhiclientapp.Models.EntregaModel
import com.example.thakhiclientapp.R
import kotlinx.android.synthetic.main.menu_activity.*
import java.text.SimpleDateFormat
import java.util.*

class MenuController : AppCompatActivity() {

    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val fechaactual = sdf.format(Date())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)

        txtDni.text = "DNI N° " + ConexionModel.dniCliente
        txtNombre.text = ConexionModel.nombreCliente

        ubicacionCliente.setOnClickListener {
            var i= Intent(this,UbicacionController::class.java)
            startActivity(i)
        }

        valoracionCliente.setOnClickListener {
            var url= ConexionModel.url + "BuscarCalificacion.php?CALfecha=" + fechaactual.toString()
            var rq= Volley.newRequestQueue(this)

            var sr= StringRequest(Request.Method.GET,url,
                Response.Listener { response ->
                    if(response=="0"){
                        var i= Intent(this,NotificacionController::class.java)
                        startActivity(i)
                    }
                    if(response=="1"){
                        ObtenerDatosEntrega(ConexionModel.dniCliente,fechaactual)
                        var i= Intent(this,AnimationController::class.java)
                        startActivity(i)
                    }
                },
                Response.ErrorListener {  })
            rq.add(sr)
        }

        Cliente.setOnClickListener {
            var i= Intent(this,ClienteController::class.java)
            startActivity(i)
        }

        logoutCliente.setOnClickListener {
            Toast.makeText(this,"Cerrando Sesión...", Toast.LENGTH_LONG).show()
            ConexionModel.dniCliente == ""
            ConexionModel.nombreCliente == ""
            var i= Intent(this,SplashController::class.java)
            startActivity(i)
        }
    }

    fun ObtenerDatosEntrega(dni:String,fecha:String) {
        //Toast.makeText(this,"DNI " + dni + " fecha: " + fecha,Toast.LENGTH_LONG).show()
        var url =
            ConexionModel.url + "ListarCalificaciones.php?CLIdni=" + dni + "&ENTfechahora=" + fecha
        var rq = Volley.newRequestQueue(this)

        val sr = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                EntregaModel.idEntrega = response.getJSONObject(0).getString("ENTid")
                EntregaModel.descripcionEntrega =
                    response.getJSONObject(0).getString("ENTdescripcion")
                EntregaModel.dniConductor = response.getJSONObject(0).getString("CONdni")
                EntregaModel.nombreConductor = response.getJSONObject(0).getString("CONnombre")
                EntregaModel.apellidoConductor = response.getJSONObject(0).getString("CONapellido")
                EntregaModel.celularConductor = response.getJSONObject(0).getString("CONcelular")
                EntregaModel.precioEntrega = response.getJSONObject(0).getDouble("ENTprecio")
                //Toast.makeText(this,"numero:" + admEnttEntrega.ENTid,Toast.LENGTH_SHORT).show()
                /*ClsConexion.dni = response.getJSONObject(0).getString("CLIdni")
                ClsConexion.nombres = response.getJSONObject(0).getString("CLInombre") + " " +
                        response.getJSONObject(0).getString("CLIapellido")

                var i= Intent(this,MainActivity::class.java)
                startActivity(i)*/
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "No hay entregas pendientes para hoy...", Toast.LENGTH_SHORT)
                    .show()
            }
        )
        rq.add(sr)
    }
}
