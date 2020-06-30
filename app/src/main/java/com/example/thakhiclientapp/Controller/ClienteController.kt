package com.example.thakhiclientapp.Controller

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.example.thakhiclientapp.Models.ConexionModel
import com.example.thakhiclientapp.R
import kotlinx.android.synthetic.main.cliente_activity.*
import kotlinx.android.synthetic.main.cliente_activity.btnGuardar
import kotlinx.android.synthetic.main.registro_activity.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ClienteController : AppCompatActivity() {

    private val CARPETA_PRINCIPAL = "misImagenesApp/"//directorio principal
    private val CARPETA_IMAGEN = "Imagenes/"//carpeta donde se guardan las fotos
    private val DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN//ruta carpeta de directorios
    private var path:String?=null//almacena la ruta de la imagen
    var fileImagen: File? = null
    var bitmap: Bitmap? = null
    private val MIS_PERMISOS = 100
    private val COD_SELECCIONA = 10
    private val COD_FOTO = 20

    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val fechaactual = sdf.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cliente_activity)

        if (solicitaPermisosVersionesSuperiores()) {
            btnfoto.setEnabled(true)
        } else {
            btnfoto.setEnabled(false)
        }

        btnfoto.setOnClickListener{
            mostrarDialogOpciones()
        }

        btnGuardar.setOnClickListener {
            cargarWebService()
        }
    }

    private fun cargarWebService(){
        btnGuardar.setOnClickListener {
            if(txtClave.text.toString() != txtConfimarClave.text.toString()){
                Toast.makeText(this,"Las contraseñas no coinciden... Intente de nuevo",
                    Toast.LENGTH_LONG).show()
            }
            else if(txtCelularCliente.text.toString()=="" && txtCorreoCliente.text.toString()=="" && txtNuevaClave.text.toString()==""){
                Toast.makeText(this,"Rellene los campos del formulario...",
                    Toast.LENGTH_LONG).show()
            }
            else{
                var url = ConexionModel.url + "ModificarCliente.php?CLIcelular=" + txtCelularCliente.text.toString() +
                        "&CLIemail=" + txtCorreoCliente.text.toString() +
                        "&CLIclave=" + txtNuevaClave.text.toString() +
                        "&CLIdni=" + ConexionModel.dniCliente
                var rq= Volley.newRequestQueue(this)
                var sr= StringRequest(Request.Method.GET,url,
                    Response.Listener { response ->
                        if(response=="0")
                            Toast.makeText(this,"Usuario no registrado...",
                                Toast.LENGTH_LONG).show()
                        else{
                            Toast.makeText(this,"Datos modificados correctamente... Ingrese nuevamente!",
                                Toast.LENGTH_LONG).show()
                            ConexionModel.dniCliente == ""
                            ConexionModel.nombreCliente== ""
                            var i= Intent(this,SplashController::class.java)
                            startActivity(i)
                        }
                    },
                    Response.ErrorListener {  })
                rq.add(sr)
            }
        }
    }

    private fun convertirImgString(bitmap: Bitmap?): String {

        val array = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, array)
        val imagenByte = array.toByteArray()
        return Base64.encodeToString(imagenByte, Base64.DEFAULT)
    }

    /*private fun guardarFoto(){
        var url = ClsConexion.url + "InsertarImagen.php?IMGnombre=" + txtcelular.text.toString() +
                "&IMGruta=" + txtcorreo.text.toString() +
                "&IMGfechaSubida=" +
        var rq= Volley.newRequestQueue(this)
        var sr= StringRequest(
            Request.Method.GET,url,
            Response.Listener { response ->
                if(response=="0")
                    Toast.makeText(this,"Usuario no registrado...",
                        Toast.LENGTH_LONG).show()
                else{
                    Toast.makeText(this,"Datos modificados correctamente... Ingrese nuevamente!",
                        Toast.LENGTH_LONG).show()
                    ClsConexion.dni == ""
                    ClsConexion.nombres == ""
                    var i= Intent(this,SplashActivity::class.java)
                    startActivity(i)
                }
            },
            Response.ErrorListener {  })
        rq.add(sr)
    }*/


    fun mostrarDialogOpciones() {
        val opciones = arrayOf<CharSequence>("Tomar Foto", "Elegir de Galeria", "Cancelar")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Elige una Opción")
        builder.setItems(opciones, DialogInterface.OnClickListener { dialogInterface, i ->
            if (opciones[i] == "Tomar Foto") {
                abriCamara()
            }
            else if (opciones[i] == "Elegir de Galeria"){
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                intent.type = "image/"
                startActivityForResult(
                    Intent.createChooser(intent, "Seleccione"),
                    COD_SELECCIONA
                )
            } else if (opciones[i] == "Cancelar"){
                dialogInterface.dismiss()
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            COD_SELECCIONA -> {
                val miPath = data?.data
                ivfoto.setImageURI(miPath)

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath)
                    ivfoto.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            COD_FOTO -> {
                MediaScannerConnection.scanFile(
                    this, path?.let { arrayOf<String>(it) }, null
                ) { path, uri -> Log.i("Path", "" + path) }

                bitmap = BitmapFactory.decodeFile(path)
                ivfoto.setImageBitmap(bitmap)
            }
        }
        bitmap = bitmap?.let { redimensionarImagen(it, 600f, 800f) }
    }

    private fun redimensionarImagen(bitmap: Bitmap, anchoNuevo: Float, altoNuevo: Float): Bitmap {
        val ancho = bitmap.width
        val alto = bitmap.height

        if (ancho > anchoNuevo || alto > altoNuevo) {
            val escalaAncho = anchoNuevo / ancho
            val escalaAlto = altoNuevo / alto

            val matrix = Matrix()
            matrix.postScale(escalaAncho, escalaAlto)

            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false)
        } else {
            return bitmap
        }
    }

    private fun abriCamara() {
        val miFile = File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN)
        var isCreada = miFile.exists()

        if (isCreada == false) {
            isCreada = miFile.mkdirs()
        }

        if (isCreada == true) {
            val consecutivo = System.currentTimeMillis() / 1000
            val nombre = "$consecutivo.jpg"

            path =
                (Environment.getExternalStorageDirectory().toString() + File.separator + DIRECTORIO_IMAGEN
                        + File.separator + nombre)//indicamos la ruta de almacenamiento

            fileImagen = File(path)

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen))

            ////
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val authorities = this.getPackageName() + ".provider"
                val imageUri = FileProvider.getUriForFile(this, authorities, fileImagen!!)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen))
            }
            startActivityForResult(intent, COD_FOTO)
        }
    }

    private fun solicitaPermisosVersionesSuperiores(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//validamos si estamos en android menor a 6 para no buscar los permisos
            return true
        }
        //validamos si los permisos ya fueron aceptados
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) === android.content.pm.PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(
                Manifest.permission.CAMERA
            ) === android.content.pm.PackageManager.PERMISSION_GRANTED
        )
        {
            return true
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(
                Manifest.permission.CAMERA
            )
        ) {
            cargarDialogoRecomendacion()
        } else {
            requestPermissions(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ), MIS_PERMISOS)
        }

        return false//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    private fun cargarDialogoRecomendacion() {
        val dialogo = AlertDialog.Builder(this)
        dialogo.setTitle("Permisos Desactivados")
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App")
        dialogo.setPositiveButton("Aceptar", object: DialogInterface.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onClick(dialogInterface: DialogInterface, i:Int) {
                requestPermissions(arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 100)
            }
        })
        dialogo.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MIS_PERMISOS) {
            if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {//el dos representa los 2 permisos
                Toast.makeText(this, "Permisos aceptados", Toast.LENGTH_SHORT)
                btnfoto.setEnabled(true)
            }
        } else {
            solicitarPermisosManual()
        }
    }

    private fun solicitarPermisosManual() {
        val opciones = arrayOf<CharSequence>("si", "no")
        val alertOpciones = AlertDialog.Builder(this)//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?")
        alertOpciones.setItems(opciones
        ) { dialogInterface, i ->
            if (opciones[i] == "si") {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", this.getPackageName(), null)
                intent.data = uri
                startActivity(intent)
            } else {
                Toast.makeText(this, "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            }
        }
        alertOpciones.show()
    }

}
