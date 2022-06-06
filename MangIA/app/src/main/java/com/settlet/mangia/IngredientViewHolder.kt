package com.settlet.mangia

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.settlet.mangia.databinding.RowIngredientRecyclerBinding
import kotlinx.android.synthetic.main.activity_mrecipe_step2.*

class IngredientViewHolder (view:View):RecyclerView.ViewHolder(view) {
    val binding = RowIngredientRecyclerBinding.bind(view)

    fun render(ingredient:Ingredient){
        binding.txvIngredient.text = ingredient.nombre
        binding.txvUnity.text = "Gr"

        binding.imvRemove.setOnClickListener {
            val a = binding.imvRemove.context as MRecipeStep2Activity
            a.listIngredientRecipe.remove(ingredient)
            a.rcvIngredients.adapter!!.notifyDataSetChanged()

        }

        binding.imbAddQuantity.setOnClickListener {
            if(binding.txpQuantity.text.toString() == "")
            {
                binding.txpQuantity.setText("1")
            }
            else{
                binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
            }
        }
        binding.imbRemoveQuantity.setOnClickListener {
            if(binding.txpQuantity.text.toString() == "0")
            {
                Toast.makeText(binding.txpQuantity.context,"No puedes tener menos de 0 ${binding.txvUnity.text} de ${ingredient.nombre} en tu receta.",Toast.LENGTH_SHORT).show()
            }
            else{
                binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
            }
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