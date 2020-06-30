package com.example.thakhiclientapp.Models

class EntregaModel (var idEntrega:String,
                    var descripcionEntrega:String,
                    var dniConductor:String,
                    var nombreConductor:String,
                    var apellidoConductor:String,
                    var celularConductor:String,
                    var precioEntrega:Double)

{
    companion object{
        var idEntrega:String=""
        var descripcionEntrega:String=""
        var dniConductor:String=""
        var nombreConductor:String=""
        var apellidoConductor:String=""
        var celularConductor:String=""
        var precioEntrega:Double=0.0
    }
}