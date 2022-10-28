package com.settlet.mangia.Adapter

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
import com.settlet.mangia.Model.ChatItem
import com.settlet.mangia.R
import com.settlet.mangia.databinding.RowMessageBinding

class ChatItemAdapter : ListAdapter<ChatItem, ChatItemAdapter.ChatItemViewHolder>(DiffCallBack){

    class ChatItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding:RowMessageBinding = RowMessageBinding.bind(itemView)
        val reference = FirebaseDatabase.getInstance().reference
        private val storageRef = FirebaseStorage.getInstance().reference
        fun render(chatItem:ChatItem){
            loadUserInfo(chatItem.userID.toString())
            loadLastMessage(chatItem.lastMessage)
            loadUnseenMessages(chatItem.unseenMessages)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context,ChatActivity::class.java)
                intent.putExtra("uid",chatItem.userID)
                intent.putExtra("name",binding.txvUNameM.text)
                itemView.context.startActivity(intent)
            }
        }

        private fun loadUnseenMessages(unseenMessages: Int?) {
            if(unseenMessages!=null){
                if (unseenMessages==0){
                    binding.imvNotifUMessageM.visibility = View.GONE
                    binding.txvUMessageM.visibility = View.GONE
                }else{

                }
            }
        }

        private fun loadLastMessage(lastMessage: String?) {
            if (lastMessage!=null)
                binding.txvLastMessage.text = lastMessage
            else
                binding.txvLastMessage.text = ""
        }

        private fun loadUserInfo(receiverID: String) {
            reference.child("users").child(receiverID).get().addOnSuccessListener {
                binding.txvUNameM.text = it.child("nickName").getValue(String::class.java)
            }
            storageRef.child("users/$receiverID/profile.jpg").downloadUrl.addOnSuccessListener { result->
                Glide.with(binding.imvProfilePictureM.context)
                    .load(result)
                    .into(binding.imvProfilePictureM)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_message,parent,false)
        return ChatItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<ChatItem>() {
        override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem.userID == newItem.userID
        }

        override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem == newItem
        }
    }
}