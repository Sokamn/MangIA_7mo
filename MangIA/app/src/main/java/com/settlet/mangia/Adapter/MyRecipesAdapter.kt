package com.settlet.mangia.Adapter

import android.content.Context
import android.content.Intent
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
import com.settlet.mangia.RecipeDetailActivity

class MyRecipesAdapter : ListAdapter<Recipe, MyRecipesAdapter.MyRecipesViewHolder>(DiffCallBack){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipesViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_gallery_recipe_recycler,parent,false)
        return MyRecipesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecipesViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            val editor = holder.itemView.context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("recipeID", item.recipeID)
            editor.apply()
            holder.itemView.context.startActivity(Intent(holder.itemView.context, RecipeDetailActivity::class.java))
        }
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
            val ic_multiImages = itemView.findViewById<ImageView>(R.id.imvIC_MultiImage)
            if (recipe.listImages.size == 1){
                ic_multiImages.visibility = View.GONE
            }else{
                ic_multiImages.visibility = View.VISIBLE
            }

        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Recipe>(){
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.recipeID == newItem.recipeID
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }

    }
}