package com.settlet.mangia.ViewHolder

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.ChatActivity
import com.settlet.mangia.Model.Message
import com.settlet.mangia.databinding.RowMessageBinding

class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = RowMessageBinding.bind(view)
    private val storageReference = FirebaseStorage.getInstance().reference
    private val reference = FirebaseDatabase.getInstance().reference

    fun render(message: Message){
        loadUserInfo(message.userID)
        getLastMessage(message.lastMessage)
        getUnseenMessages(message.unseenMessages)

        itemView.setOnClickListener {
            val intent = Intent(binding.imvProfilePictureM.context,ChatActivity::class.java)
            intent.putExtra("name",binding.txvUNameM.text.toString())
            intent.putExtra("getUserID",message.userID)
            intent.putExtra("chat_key",message.chatKey)
            binding.imvProfilePictureM.context.startActivity(intent)
        }
    }

    private fun getUnseenMessages(unseenMessages: Int) {
        if(unseenMessages==0){
            binding.imvNotifUMessageM.visibility = View.GONE
            binding.txvUMessageM.visibility = View.GONE
        }else{
            binding.imvNotifUMessageM.visibility = View.VISIBLE
            binding.txvUMessageM.visibility = View.VISIBLE
            binding.txvUMessageM.text = unseenMessages.toString()
        }
    }

    private fun getLastMessage(lastMessage: String) {
        binding.txvLastMessage.text = lastMessage
    }


    private fun loadUserInfo(userID: String) {
        val pImageRef = storageReference.child("users/$userID/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(binding.imvProfilePictureM.context)
                .load(result)
                .into(binding.imvProfilePictureM)
        }
        reference.child("users").child(userID).get().addOnSuccessListener {
            binding.txvUNameM.text = it.child("userName").value.toString()
        }

    }
}