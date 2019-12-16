package com.example.segundoproyecto

import java.io.Serializable

data class Contacto (var id:Int, var nombre:String, var numero:String, var img:String, var direccion:String, var favorito:String) : Serializable