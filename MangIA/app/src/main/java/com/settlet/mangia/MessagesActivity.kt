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
        unseenMessages = 0
        lastMessage = ""
        chatKey = ""
        followingList.forEach { userID->
                dataSet = false
                reference.child("chat").addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val childrenCount = snapshot.childrenCount
                        if(childrenCount>0){
                            snapshot.children.forEach{ dataSnapshot1->
                                val key = dataSnapshot1.key
                                chatKey = key!!
                                if(dataSnapshot1.hasChild("user_1")&&dataSnapshot1.hasChild("user_2")&&dataSnapshot1.hasChild("messages")){
                                    val userOne = dataSnapshot1.child("user_1").getValue(String::class.java)
                                    val userTwo = dataSnapshot1.child("user_2").getValue(String::class.java)

                                    if(userOne!! == userID && userTwo!! == currentUser || userOne == currentUser && userTwo!! == userID){
                                        dataSnapshot1.child("messages").children.forEach { chatDataSnapshot ->
                                            val messageKey = chatDataSnapshot.key!!.toLong()
                                            var getLastMessage: Long = 0
                                            val lastmsg = MemoryData.getLastMsgTS(this@MessagesActivity, key)
                                            if(!lastmsg.isEmpty()){
                                                getLastMessage = lastmsg.toLong()
                                            }
                                            lastMessage = chatDataSnapshot.child("msg").getValue(String::class.java).toString()
                                            if(messageKey > getLastMessage){
                                                unseenMessages++
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(!dataSet){
                            dataSet = true
                            //val message = Message(userID,lastMessage,chatKey,unseenMessages)
                            //messageList.add(message)
                            //messageAdapter.updateData(messageList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

        }
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