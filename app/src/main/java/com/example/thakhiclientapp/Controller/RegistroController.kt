package com.example.thakhiclientapp.Controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.thakhiclientapp.Models.ConexionModel

import com.example.thakhiclientapp.R
import com.google.android.material.drawable.DrawableUtils
import kotlinx.android.synthetic.main.registro_activity.*

class RegistroController : AppCompatActivity() {

    //val CAMERA_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_activity)

        //btnCargar.setOnClickListener{val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //if(callCameraIntent.resolveActivity(packageManager) != null) {
                //startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            //}
        //}

        btnGuardar.setOnClickListener {
            if(txtDni.text.toString().equals("") || txtCelular.text.toString().equals("") || txtCorreo.text.toString().equals("") ||
                txtClave.text.toString().equals("") || txtConfirma.text.toString().equals("")){
                Toast.makeText(this,"Necesita rellenar todos los campos...",Toast.LENGTH_LONG).show()
            } else {
                if(txtClave.text.toString() == txtConfirma.text.toString()){
                    var url = ConexionModel.url + "RegistroCliente.php?CLIdni=" + txtDni.text.toString() +
                            "&CLIcelular=" + txtCelular.text.toString() +
                            "&CLIemail=" + txtCorreo.text.toString() +
                            "&CLIclave=" + txtClave.text.toString()
                    var rq= Volley.newRequestQueue(this)
                    var sr= StringRequest(
                        Request.Method.GET,url,
                        Response.Listener { response ->
                            if(response=="0")
                                Toast.makeText(this,"El cliente ya se encuentra registrado...",
                                    Toast.LENGTH_LONG).show()
                            else{
                                Toast.makeText(this,"Datos guardados correctamente...",
                                    Toast.LENGTH_LONG).show()
                                var i= Intent(this,LoginController::class.java)
                                startActivity(i)
                            }
                        },
                        Response.ErrorListener {  })
                    rq.add(sr)
                }
                else{
                    Toast.makeText(this,"Las contraseñas no coinciden... Intente de nuevo",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
