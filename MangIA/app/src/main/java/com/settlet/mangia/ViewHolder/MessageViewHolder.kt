package com.settlet.mangia.ViewHolder

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.ChatActivity
import com.settlet.mangia.Model.Message
import com.settlet.mangia.databinding.RowMessageBinding

class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = RowMessageBinding.bind(view)
    private val storageReference = FirebaseStorage.getInstance().reference

    fun render(message: Message){
        loadUserLogo(message.userID)
        getLastMessage(message.lastMessage)
        getUnseenMessages(message.unseenMessages)

        itemView.setOnClickListener {
            val intent = Intent(binding.imvProfilePictureM.context,ChatActivity::class.java)
            intent.putExtra("name",binding.txvUNameM.text.toString())
            intent.putExtra("messageID",message.userID)
            intent.putExtra("chat_key",message.chatKey)
        }
    }

    private fun getUnseenMessages(unseenMessages: Int) {
        if(unseenMessages==0){
            binding.imvNotifUMessageM.visibility = View.GONE
            binding.txvUMessageM.visibility = View.GONE
            binding.txvLastMessage.setTextColor(Color.parseColor("#asdasd"))
        }else{
            binding.imvNotifUMessageM.visibility = View.VISIBLE
            binding.txvUMessageM.visibility = View.VISIBLE
            binding.txvUMessageM.text = unseenMessages.toString()
            binding.txvLastMessage.setTextColor(Color.parseColor("#asdasd"))
        }
    }

    private fun getLastMessage(lastMessage: String) {
        binding.txvLastMessage.text = lastMessage
    }


    private fun loadUserLogo(userID: String) {
        val pImageRef = storageReference.child("users/$userID/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(binding.imvProfilePictureM.context.applicationContext)
                .load(result)
                .into(binding.imvProfilePictureM)
        }
    }
}