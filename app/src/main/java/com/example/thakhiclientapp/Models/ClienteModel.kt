package com.example.thakhiclientapp.Models

class ClienteModel (var dniCliente:String,
                    var nombreCliente:String,
                    var apellidoCliente:String,
                    var celularCliente:String,
                    var correoCliente:String,
                    var claveCliente:String,
                    var latitudCliente:Double,
                    var longitudCliente:Double,
                    var fotoCliente:String)
{
    companion object{

        var dniCliente:String=""
        var nombreCliente:String=""
        var apellidoCliente:String=""
        var celularCliente:String=""
        var correoCliente:String=""
        var claveCliente:String=""
        var latitudCliente:Double=0.0
        var longitudCliente:Double=0.0
        var fotoCliente:String=""
    }

}