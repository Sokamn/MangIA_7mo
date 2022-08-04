package com.settlet.mangia.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.databinding.RowUserRateBinding
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class UserRateViewHolder (view: View): RecyclerView.ViewHolder(view)  {
    private val db = Firebase.firestore
    val binding = RowUserRateBinding.bind(view)
    private val storageReference = FirebaseStorage.getInstance().reference

    fun render(userRate: Array<String>){
        binding.txvUNameUR.text = userRate[1]
        binding.txvNNameUR.text = userRate[2]
        binding.txvRateUR.text = userRate[3]
        val pImageRef = storageReference.child("users/" + userRate[0] + "/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(binding.imvProfilePictureUR.context)
                .load(result)
                .into(binding.imvProfilePictureUR)
        }
    }
}