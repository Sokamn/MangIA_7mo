package com.settlet.mangia.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.RowUserBinding

class UserViewHolder(view: View): RecyclerView.ViewHolder(view)  {
    val binding = RowUserBinding.bind(view)
    val db = Firebase.firestore
    private val storageReference = FirebaseStorage.getInstance().reference
    private val defaultPImage = storageReference.child("profilePicture/profile_picture.jpg")

    fun render(user: User){
        binding.txvNNameRU.text = user.nickName
        binding.txvUNameRU.text = user.userName

        val pImageRef = storageReference.child("users/" + user.email + "/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(itemView.context)
                .load(result)
                .into(binding.imvProfilePictureRU)
        }
            .addOnFailureListener {
                defaultPImage.downloadUrl.addOnSuccessListener { result ->
                    Glide.with(itemView.context)
                        .load(result)
                        .into(binding.imvProfilePictureRU)
                }
            }

        binding.btnFollowRU.setOnClickListener {

        }
    }
}