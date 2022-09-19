package com.settlet.mangia.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.RecipeSearchedViewHolder

class RecipeSearchedAdapter : ListAdapter<Recipe, RecipeSearchedViewHolder>(DiffCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeSearchedViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_recipe_searched,parent,false)
        return RecipeSearchedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeSearchedViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.recipeID == newItem.recipeID
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }
}