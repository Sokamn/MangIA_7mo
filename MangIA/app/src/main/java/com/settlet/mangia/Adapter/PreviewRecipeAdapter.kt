package com.settlet.mangia.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.PreviewRecipeViewHolder

class PreviewRecipeAdapter: ListAdapter<Recipe, PreviewRecipeViewHolder>(DiffCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewRecipeViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recipe_item,parent,false)
        return PreviewRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreviewRecipeViewHolder, position: Int) {
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