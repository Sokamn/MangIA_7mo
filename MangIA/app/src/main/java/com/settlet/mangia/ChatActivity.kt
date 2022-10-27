package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatBinding
    private lateinit var chatKey: String
    private lateinit var messageID: String
    private var generatedChatKey: Int = 1

    private val reference = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        loadUserInfo()

        if(chatKey.isEmpty()){
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    generatedChatKey = 1
                    if(snapshot.hasChild("chat")){
                        generatedChatKey = snapshot.child("chat").childrenCount.toInt() + 1
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        binding.btnSendMessage.setOnClickListener {

        }

    }

    private fun loadUserInfo() {
        val pImageRef = FirebaseStorage.getInstance().reference.child("users/${intent.getStringExtra("messageID")}/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(this)
                .load(result)
                .into(binding.imvProfilePictureChat)
        }
        binding.txvUNameM2.text = intent.getStringExtra("name").toString()
        chatKey = intent.getStringExtra("chat_key").toString()
        messageID = intent.getStringExtra("messageID").toString()

    }
}