package com.settlet.mangia.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.IngredientViewHolder


class IngredientAdapter(private val ingredientList: List<Ingredient>) : RecyclerView.Adapter<IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IngredientViewHolder(layoutInflater.inflate(R.layout.row_ingredient_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val item = ingredientList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = ingredientList.size
}
