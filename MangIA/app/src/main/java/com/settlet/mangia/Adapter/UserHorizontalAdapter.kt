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
import com.settlet.mangia.ChatActivity
import com.settlet.mangia.Model.User
import com.settlet.mangia.R
import com.settlet.mangia.databinding.UserHorizontalItemBinding

class UserHorizontalAdapter : ListAdapter<User, UserHorizontalAdapter.StringViewHolder>(DiffCallBack){

    class StringViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding: UserHorizontalItemBinding = UserHorizontalItemBinding.bind(itemView)
        val reference = FirebaseDatabase.getInstance().reference
        private val storageRef = FirebaseStorage.getInstance().reference
        fun render(user: User){
            loadUserInfo(user.userID)
        }

        private fun loadUserInfo(userID: String) {
            reference.child("users").child(userID).child("nickName").get().addOnSuccessListener {
                binding.txvNickNameM2.text = it.value.toString()
            }

                storageRef.child("users/$userID/profile.jpg").downloadUrl.addOnSuccessListener { result ->
                    Glide.with(binding.shapeableImageView.context.applicationContext)
                        .load(result)
                        .into(binding.shapeableImageView)
                }.addOnFailureListener {
                    Glide.with(binding.shapeableImageView.context.applicationContext)
                        .load(R.drawable.profile_picture)
                        .into(binding.shapeableImageView)
                }

        }

    }
    fun updateUsers(listUser: List<User>){
        this.submitList(listUser)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.user_horizontal_item,parent,false)
        return StringViewHolder(view)
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("uid",item.userID)
            intent.putExtra("name",holder.binding.txvNickNameM2.text)
            holder.itemView.context.startActivity(intent)
        }
        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}