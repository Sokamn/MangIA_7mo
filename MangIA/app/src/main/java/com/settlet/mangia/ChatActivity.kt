package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.MemoryData
import com.settlet.mangia.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatBinding
    private lateinit var chatKey: String
    private lateinit var userID: String
    private val currentUser = Firebase.auth.currentUser!!.uid

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
                    chatKey = "1"
                    if(snapshot.hasChild("chat")){
                        chatKey = (snapshot.child("chat").childrenCount.toInt() + 1).toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        binding.btnSendMessage.setOnClickListener {
            if (binding.txpSendMessage.text.isNotEmpty()){
                val currentTime = System.currentTimeMillis().toString().substring(0,10)
                MemoryData.saveLastMsgTS(currentTime,chatKey,this@ChatActivity)
                reference.child("chat").child(chatKey).child("user_1").setValue(currentUser)
                reference.child("chat").child(chatKey).child("user_2").setValue(userID)
                reference.child("chat").child(chatKey).child("messages").child(currentTime).child("msg").setValue(binding.txpSendMessage.text.toString())
                reference.child("chat").child(chatKey).child("messages").child(currentTime).child("user").setValue(currentUser)

                binding.txpSendMessage.setText("")
            }
        }

    }

    private fun loadUserInfo() {
        binding.txvUNameM2.text = intent.getStringExtra("name").toString()
        chatKey = intent.getStringExtra("chat_key").toString()
        userID = intent.getStringExtra("getUserID").toString()
        val pImageRef = FirebaseStorage.getInstance().reference.child("users/$userID/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(this)
                .load(result)
                .into(binding.imvProfilePictureChat)
        }
    }
}