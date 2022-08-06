package com.settlet.mangia.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.R

class MyRecipesAdapter : ListAdapter<Recipe, MyRecipesAdapter.MyRecipesViewHolder>(DiffCallBack){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipesViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_gallery_recipe_recycler,parent,false)
        return MyRecipesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecipesViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)
    }

    class MyRecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val storageReference = FirebaseStorage.getInstance().reference

        fun render(recipe: Recipe){
            storageReference.child(recipe.listImages.first()).downloadUrl.addOnSuccessListener {
                val recipeImage = itemView.findViewById<ImageView>(R.id.row_image)
                Glide.with(itemView.context)
                    .load(it)
                    .apply(RequestOptions().centerCrop())
                    .into(recipeImage)
            }
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Recipe>(){
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.recipeID == newItem.recipeID
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.equals(newItem)
        }

    }
}