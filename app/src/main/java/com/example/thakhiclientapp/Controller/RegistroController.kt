package com.example.thakhiclientapp.Controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.thakhiclientapp.Models.ConexionModel

import com.example.thakhiclientapp.R
import kotlinx.android.synthetic.main.registro_activity.*

class RegistroController : AppCompatActivity() {

    val CAMERA_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_activity)

        btnCargar.setOnClickListener{val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }

        btnGuardar.setOnClickListener {
            if(txtClave.text.toString() == txtConfirma.text.toString()){
                var url = ConexionModel.url + "RegistroCliente.php?CLIdni=" + txtDni.text.toString() +
                        "&CLInombre=" + txtNombre.text.toString() +
                        "&CLIapellido=" + txtApellido.text.toString() +
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    iv_foto.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
