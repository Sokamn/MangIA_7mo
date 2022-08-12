package com.settlet.mangia.Model

import com.google.firebase.storage.StorageReference

data class Ingredient(internal var nombre:String = "", internal var tipoUnidad: String = "", internal var costo: Float = 0F, internal var cantidad:Int = 0, internal var imgRef: StorageReference? = null) {
    var unidad = "Un"
    var cant = 0
}