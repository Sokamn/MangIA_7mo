package com.settlet.mangia.Model

import com.google.firebase.storage.StorageReference

class Ingredient (internal val nombre:String, internal val tipoUnidad: String, internal var costo: Float, internal var cantidad:Int = 0, internal val imgRef: StorageReference) {
}