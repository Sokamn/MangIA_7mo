package com.settlet.mangia.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.R
import com.settlet.mangia.databinding.RowIngredientRecipeBinding

class IngredientRecipeAdapter : ListAdapter<Ingredient, IngredientRecipeAdapter.IngredientRecipeViewHolder>(DiffCallBack){
    class IngredientRecipeViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val binding = RowIngredientRecipeBinding.bind(view)
        fun render(ingredient: Ingredient){
            var unidad = ""
            if (ingredient.cantidad == 1){
                unidad = when(ingredient.unidad){
                    "Atd"-> "atado"
                    "Gr"-> "gramo"
                    "Kg"-> "kilogramo"
                    "Hjs"-> "hoja"
                    "Un"-> "unidad"
                    "Lts"-> "litro"
                    "Ml"-> "mililitro"
                    "Cm³"-> "centimetro cubico"
                    "Tz"-> "taza"
                    "C/s"-> "cucharada sopera"
                    "C/c"-> "cucharadita"
                    else -> "nada"
                }
            }else{
                unidad = when(ingredient.unidad){
                    "Atd"-> "atados"
                    "Gr"-> "gramos"
                    "Kg"-> "kilogramos"
                    "Hjs"-> "hojas"
                    "Un"-> "unidades"
                    "Lts"-> "litros"
                    "Ml"-> "mililitros"
                    "Cm³"-> "centimetro cubicos"
                    "Tz"-> "tazas"
                    "C/s"-> "cucharadas sopera"
                    "C/c"-> "cucharaditas"
                    else -> "nada"
                }
            }

            binding.txvIngredientANDQuantities.text = "${ingredient.cantidad} $unidad de ${ingredient.nombre}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientRecipeViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_ingredient_recipe,parent,false)
        return IngredientRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientRecipeViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Ingredient>(){
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.nombre == newItem.nombre
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem == newItem
        }

    }

}