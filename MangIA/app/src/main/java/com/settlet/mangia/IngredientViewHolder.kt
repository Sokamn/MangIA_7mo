package com.settlet.mangia

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.settlet.mangia.databinding.RowIngredientRecyclerBinding

class IngredientViewHolder (view:View):RecyclerView.ViewHolder(view) {
    val binding = RowIngredientRecyclerBinding.bind(view)

    fun render(ingredient:Ingredient){
        binding.txvIngredient.text = ingredient.nombre
        binding.txvUnity.text = "Gr"

        binding.imvRemove.setOnClickListener {
            val a = binding.imvRemove.context as MRecipeStep2Activity
            a.listIngredientRecipe.remove(ingredient)
            a.initRcView(a.listIngredientRecipe)
        }

        binding.imbAddQuantity.setOnClickListener {
            ingredient.cantidad++
        }
        binding.imbRemoveQuantity.setOnClickListener {
            ingredient.cantidad--
        }
        binding.imvExpandUnity.setOnClickListener {

        }
        ingredient.imgRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(binding.imvIngredient.context)
                .load(result)
                .into(binding.imvIngredient)

        }.addOnFailureListener {

        }
    }
}