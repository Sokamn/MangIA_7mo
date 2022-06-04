package com.settlet.mangia

import com.google.firebase.storage.StorageReference

class Ingredient (val nombre:String, val tipoUnidad: String, var costo: Float, var cantidad:Int, val imgRef: StorageReference) {
}