package com.example.thakhiclientapp.Controller

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.thakhiclientapp.Models.ConexionModel
import com.example.thakhiclientapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.ubicacion_activity.*

class UbicacionController : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    var lat: Double = 0.0
    var lon: Double = 0.0

    private lateinit var fusedLocationClient : FusedLocationProviderClient

    private lateinit var lastLocation : Location

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    /*codigo para sobreescribir lo que queremos hacer en el mapa*/
    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ubicacion_activity)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapCliente) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnUbicacion.setOnClickListener {
            //codigo para guardar la latitud y longitud en la base de datos
            setUpMap()
            var url = ConexionModel.url + "CapturarUbicacion.php?CLIlatitud=" + lat + "&CLIlongitud=" + lon + "&CLIdni=" + ConexionModel.dniCliente
            var rq = Volley.newRequestQueue(this)
            var sr = StringRequest(
                Request.Method.GET, url,
                Response.Listener { response ->
                    Toast.makeText(this,"Se ha enviado su Ubicacion!", Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { })
            rq.add(sr)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener(this)

        //me permite hacer zoom al mapa
        mMap.uiSettings.isZoomControlsEnabled = true

        setUpMap()
    }

    private fun placeMarker(location: LatLng){
        val markerOptions = MarkerOptions().position(location)
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_ubicacion))
        mMap.addMarker(markerOptions)
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                lat = location.latitude
                lon = location.longitude
                //GuardarUbicacion(ClsConexion.dni,location.latitude, location.longitude)

                placeMarker(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
            }
        }
    }
}
