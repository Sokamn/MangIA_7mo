package com.settlet.mangia.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.R
import com.settlet.mangia.databinding.UserHorizontalItemBinding

class UserHorizontalAdapter : ListAdapter<String, UserHorizontalAdapter.StringViewHolder>(DiffCallBack){

    class StringViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding: UserHorizontalItemBinding = UserHorizontalItemBinding.bind(itemView)
        val reference = FirebaseDatabase.getInstance().reference
        private val storageRef = FirebaseStorage.getInstance().reference
        fun render(userID: String){
            loadUserInfo(userID)
        }

        private fun loadUserInfo(userID: String) {
            reference.child("users").child(userID).child("nickName").get().addOnSuccessListener {
                binding.txvNickNameM2.text = it.value.toString()
            }
            storageRef.child("users/$userID/profile.jpg").downloadUrl.addOnSuccessListener { result ->
                Glide.with(binding.shapeableImageView.context)
                    .load(result)
                    .into(binding.shapeableImageView)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.user_horizontal_item,parent,false)
        return StringViewHolder(view)
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}