package com.settlet.mangia.ViewHolder

import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Adapter.SliderAdapter
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.databinding.RecipeItemBinding
import com.settlet.mangia.databinding.RowStepMrBinding
import org.imaginativeworld.whynotimagecarousel.utils.setImage

class PreviewRecipeViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding = RecipeItemBinding.bind(view)
    private val storageReference = FirebaseStorage.getInstance().reference

    fun render(recipe:Recipe){
        if (recipe.listImages.size == 1){
            Glide.with(binding.imvUniquePost.context)
                .load(recipe.listImages.first().toUri())
                .into(binding.imvUniquePost)
            binding.imgsldrCarruselRI.visibility = View.GONE
        }else{
            binding.imgsldrCarruselRI.setSliderAdapter(SliderAdapter(recipe.listImages as MutableList<String>,true))
            binding.imvUniquePost.visibility = View.INVISIBLE
        }
        val pImageRef = storageReference.child("users/${recipe.publisher}/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(binding.imvProfilePictureRI.context)
                .load(result)
                .into(binding.imvProfilePictureRI)
        }
        binding.txvDescription.text = ""
    }
}