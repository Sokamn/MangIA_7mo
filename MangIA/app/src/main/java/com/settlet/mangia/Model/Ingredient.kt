package com.settlet.mangia.Model

import com.google.firebase.storage.StorageReference

data class Ingredient(internal val nombre:String = "", internal val tipoUnidad: String = "", internal var costo: Float = 0F, internal var cantidad:Int = 0, internal val imgRef: StorageReference? = null) {
    var unidad = "Un"
    var cant = 0
}