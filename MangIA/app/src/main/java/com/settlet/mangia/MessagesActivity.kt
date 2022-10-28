package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.MessageAdapter
import com.settlet.mangia.Model.Message
import com.settlet.mangia.databinding.ActivityMessagesBinding

class MessagesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMessagesBinding
    private var messageList = mutableListOf<Message>()
    private val followingList = mutableListOf<String>()
    private val reference = FirebaseDatabase.getInstance().reference
    private val currentUser = Firebase.auth.currentUser!!.uid
    private lateinit var messageAdapter: MessageAdapter
    private var dataSet = false
    private var unseenMessages = 0
    private var lastMessage = ""
    private var chatKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CheckFollowing()
        binding.bottomNav.add(MeowBottomNavigation.Model(0,R.drawable.ic_home_menu))
        binding.bottomNav.add(MeowBottomNavigation.Model(1,R.drawable.ic_scan_nav))
        binding.bottomNav.add(MeowBottomNavigation.Model(2,R.drawable.ic_comment_recipe_black))
        binding.bottomNav.show(2,true)

        //messageAdapter = MessageAdapter()
        binding.rcvMessages.adapter = messageAdapter

        binding.bottomNav.setOnClickMenuListener {
            when(it.id){
                0->{
                    this.startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                1->{
                    this.startActivity(Intent(this, ScanCameraActivity::class.java))
                    finish()
                }
                2->{

                }
                else->{

                }
            }
        }
    }

    private fun loadMessages() {

    }

    private fun CheckFollowing(){
        reference.child("follow").child(Firebase.auth.currentUser!!.uid).child("following").get().addOnSuccessListener {
            followingList.clear()
            it.children.forEach { userID ->
                followingList.add(userID.key.toString())
            }
            loadMessages()
        }

    }
}