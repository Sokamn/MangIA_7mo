package com.settlet.mangia.ViewHolder

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.User
import com.settlet.mangia.ProfileActivity
import com.settlet.mangia.R
import com.settlet.mangia.databinding.RowUserBinding

class UserViewHolder(view: View): RecyclerView.ViewHolder(view)  {
    val binding = RowUserBinding.bind(view)
    val reference = FirebaseDatabase.getInstance().reference
    val db = Firebase.firestore
    private val storageReference = FirebaseStorage.getInstance().reference

    fun render(user: User){
        binding.txvNNameRU.text = user.nickName
        binding.txvUNameRU.text = user.userName

        val pImageRef = storageReference.child("users/" + user.userID + "/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(itemView.context)
                .load(result)
                .into(binding.imvProfilePictureRU)
        }.addOnFailureListener {
            Glide.with(itemView.context)
                .load(R.drawable.profile_picture)
                .into(binding.imvProfilePictureRU)
        }

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, ProfileActivity::class.java)
            val prefs = itemView.context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
            val profileID = prefs.getString("profileID","none")
            intent.putExtra("preProfileID",profileID)
            val editor = itemView.context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileID", user.userID)
            editor.apply()
            itemView.context.startActivity(intent)
        }
        binding.btnFollowRU.setOnClickListener{
            val currentUserID = Firebase.auth.currentUser!!.uid
            if(binding.btnFollowRU.text == "Seguir"){
                reference.child("follow").child(currentUserID).child("following").child(user.userID).setValue(true)
                reference.child("follow").child(user.userID).child("followers").child(currentUserID).setValue(true)
            }
            else{
                reference.child("follow").child(currentUserID).child("following").child(user.userID).removeValue()
                reference.child("follow").child(user.userID).child("followers").child(currentUserID).removeValue()
            }
        }
    }
}