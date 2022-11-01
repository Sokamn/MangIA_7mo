package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.ChatItemAdapter
import com.settlet.mangia.Adapter.MessageAdapter
import com.settlet.mangia.Adapter.UserHorizontalAdapter
import com.settlet.mangia.Model.ChatItem
import com.settlet.mangia.Model.Message
import com.settlet.mangia.databinding.ActivityMessagesBinding

class MessagesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMessagesBinding
    private var chatItemList = mutableListOf<ChatItem>()
    private val followingList = mutableListOf<String>()
    private val reference = FirebaseDatabase.getInstance().reference
    private val currentUser = FirebaseAuth.getInstance().uid
    private val userHorizontalAdapter = UserHorizontalAdapter()
    private lateinit var chatItemAdapter: ChatItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CheckFollowing()
        binding.bottomNav.add(MeowBottomNavigation.Model(0,R.drawable.ic_home_menu))
        binding.bottomNav.add(MeowBottomNavigation.Model(1,R.drawable.ic_scan_nav))
        binding.bottomNav.add(MeowBottomNavigation.Model(2,R.drawable.ic_comment_recipe_black))
        binding.bottomNav.show(2,true)

        chatItemAdapter = ChatItemAdapter()
        binding.rcvMessages.adapter = chatItemAdapter
        chatItemAdapter.submitList(chatItemList)

        binding.rcvHorizontalUsers.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        binding.rcvMessages.layoutManager = LinearLayoutManager(this)



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

    private fun initRecyclerView() {
        userHorizontalAdapter.submitList(followingList)
        binding.rcvHorizontalUsers.adapter = userHorizontalAdapter
    }

    private fun loadMessages() {
        followingList.forEach { userID->
            val chatItem = ChatItem(userID,"",0)
            chatItemList.add(chatItem)
            chatItemAdapter.notifyItemInserted(chatItemList.size-1)
            reference.child("chats").child(currentUser+userID).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatItem.lastMessage = snapshot.child("lastMsg").getValue(String::class.java)
                    var i = 0
                    chatItemList.forEach {
                        if(it.userID!=chatItem.userID){
                            i++
                        }else{
                            chatItemAdapter.notifyItemChanged(i)
                        }
                    }
                    Log.d("Prg",chatItemList.toString())
                }

                override fun onCancelled(error: DatabaseError) {}
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
            initRecyclerView()
        }

    }
}