package com.settlet.mangia.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.PreviewRecipeViewHolder

class PreviewRecipeAdapter (private var context: Context, private var recipeList:MutableList<Recipe>) : RecyclerView.Adapter<PreviewRecipeViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreviewRecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recipe_item,parent,false)
        return PreviewRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreviewRecipeViewHolder, position: Int) {
        holder.render(recipeList[position])
    }

    override fun getItemCount() = recipeList.size
}