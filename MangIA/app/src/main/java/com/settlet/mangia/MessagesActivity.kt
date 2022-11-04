package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doOnTextChanged
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
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.ActivityMessagesBinding

class MessagesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMessagesBinding
    private var chatItemList = mutableListOf<ChatItem>()
    private val followingList = mutableListOf<String>()
    private val userList = mutableListOf<User>()
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
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.rcvHorizontalUsers.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        binding.rcvMessages.layoutManager = LinearLayoutManager(this)

        binding.txpSearchAS2.doOnTextChanged { text, start, before, count ->
            val usersFiltered = userList.filter { user ->
                user.userName.lowercase().contains(text.toString().lowercase())||user.nickName.lowercase().contains(text.toString().lowercase())
            }
            if (text == "") {
                userHorizontalAdapter.updateUsers(mutableListOf())
            } else {
                userHorizontalAdapter.updateUsers(usersFiltered)
            }
        }

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

    private fun loadUserList() {
        binding.rcvHorizontalUsers.adapter = userHorizontalAdapter
        reference.child("users").get().addOnSuccessListener { userSnap ->
            userList.clear()
            userSnap.children.forEach { userValue ->
                val user = userValue.getValue(User::class.java)
                if (user != null) {
                    if(followingList.contains(user.userID)){
                        userList.add(user)
                        userHorizontalAdapter.submitList(userList)
                        userHorizontalAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun loadMessages() {
        followingList.forEach { userID->
            val chatItem = ChatItem(userID,"","",0)
            chatItemList.add(chatItem)
            chatItemAdapter.notifyItemInserted(chatItemList.size-1)
            reference.child("chats").child(userID+currentUser).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var i = 0
                    var j = 0
                    snapshot.child("messages").children.forEach {
                        if(it.child("senderID").getValue(String::class.java)==userID && it.child("seen").getValue(Boolean::class.java) == false){
                            i++
                        }
                    }
                    chatItem.unseenMessages = i
                    chatItem.lastMessage = snapshot.child("lastMsg").getValue(String::class.java)
                    chatItem.hourLastMessage = snapshot.child("hour").getValue(String::class.java)

                    chatItemList.forEach {
                        if(it.userID!=chatItem.userID){
                            j++
                        }else{
                            chatItemAdapter.notifyItemChanged(j)
                        }
                    }
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
            loadUserList()
        }

    }
}