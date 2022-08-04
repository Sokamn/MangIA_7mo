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
import com.google.firebase.storage.StorageReference
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.IngredientViewHolder

class MyRecipesAdapter : ListAdapter<StorageReference, MyRecipesAdapter.MyRecipesViewHolder>(DiffCallBack){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipesViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_gallery_recycler,parent,false)
        return MyRecipesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecipesViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)
    }

    class MyRecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun render(picture:StorageReference){
            picture.downloadUrl.addOnSuccessListener {
                val recipeImage = itemView.findViewById<ImageView>(R.id.row_image)
                Glide.with(itemView.context)
                    .load(it)
                    .apply(RequestOptions().centerCrop())
                    .into(recipeImage)
            }
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<StorageReference>(){
        override fun areItemsTheSame(oldItem: StorageReference, newItem: StorageReference): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: StorageReference, newItem: StorageReference): Boolean {
            return oldItem == newItem
        }

    }
}