package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.MessageAdapter
import com.settlet.mangia.Model.MemoryData
import com.settlet.mangia.Model.Message
import com.settlet.mangia.databinding.ActivityChatBinding
import com.settlet.mangia.databinding.ActivityMessagesBinding
import kotlinx.android.synthetic.main.bottom_bar.view.*

class MessagesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMessagesBinding
    private lateinit var messageList :MutableList<Message>
    private val followingList = mutableListOf<String>()
    private val reference = FirebaseDatabase.getInstance().reference
    private lateinit var messageAdapter: MessageAdapter
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
        binding.rcvMessages.setHasFixedSize(true)
        binding.rcvMessages.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter()
        followingList.forEach { followedID ->
            unseenMessages = 0
            lastMessage = ""
            chatKey = ""
            reference.child("chat").addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.childrenCount>0){
                            for (dataSnapshot1:DataSnapshot in snapshot.children){
                                var key = dataSnapshot1.key
                                chatKey = key!!
                                var userOne = dataSnapshot1.child("user_1").getValue(String::class.java)
                                var userTwo = dataSnapshot1.child("user_2").getValue(String::class.java)

                                if(userOne!!.equals(followedID) && userTwo!!.equals(Firebase.auth.currentUser)||userOne.equals(Firebase.auth.currentUser) && userTwo!!.equals(followedID)){
                                    for(chatDataSnapshot: DataSnapshot in dataSnapshot1.child("messages").children){
                                        var messageKey = chatDataSnapshot.key!!.toLong()
                                        var getLastMessage = MemoryData.getLastMsgTS(this@MessagesActivity, key!!).toLong()

                                        lastMessage = chatDataSnapshot.child("msg").getValue(String::class.java).toString()
                                        if(messageKey > getLastMessage){
                                            unseenMessages++
                                        }
                                    }
                                }
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            messageList.add(Message(followedID,lastMessage,chatKey,unseenMessages))
            messageAdapter.updateData(messageList)

        }
        binding.rcvMessages.adapter = messageAdapter
    }

    private fun CheckFollowing(){
        reference.child("follow").child(Firebase.auth.currentUser.toString()).child("following").get().addOnSuccessListener {
            messageList.clear()
            followingList.clear()
            it.children.forEach { userID ->
                followingList.add(userID.key.toString())
                Log.d("followingList", followingList.toString())
            }
            loadMessages()
        }
    }
}