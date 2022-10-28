package com.settlet.mangia

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Adapter.MessageAdapter
import com.settlet.mangia.Model.Message
import com.settlet.mangia.databinding.ActivityChatBinding
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var adapter: MessageAdapter? = null
    private var messages = mutableListOf<Message>()
    private var senderRoom: String? = null
    private var receiverRoom: String? = null
    private val reference = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference
    private var dialog: ProgressDialog? = null
    private var senderUid: String? = null
    private var receiverUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        dialog = ProgressDialog(this@ChatActivity)
        dialog!!.setMessage("Subiendo imagen...")
        dialog!!.setCancelable(false)
        receiverUid = intent.getStringExtra("uid")
        senderUid = FirebaseAuth.getInstance().uid
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid
        adapter = MessageAdapter(this@ChatActivity,senderRoom!!,receiverRoom!!)
        adapter!!.submitList(messages)
        binding.rcvMessagesChat.layoutManager = LinearLayoutManager(this@ChatActivity)
        binding.rcvMessagesChat.adapter = adapter
        val name = intent.getStringExtra("name")
        binding.txvUNameM2.text = name
        val pImageRef = storageReference.child("users/${receiverUid}/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            if (!this.isFinishing && !this.isDestroyed) {
                Glide.with(this@ChatActivity)
                    .load(result)
                    .into(binding.imvProfilePictureChat)
            }
        }

        binding.imbBackAChat.setOnClickListener {
            finish()
            onBackPressed()
        }
        Log.d("Msg",senderRoom!!.toString())
        reference.child("chats").child(senderRoom.toString()).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                Log.d("Msg","entré")
                snapshot.children.forEach { snapMessage ->
                    Log.d("Msg","entré?")
                    val message = Message(snapMessage.key,snapMessage.child("message").getValue(String::class.java),snapMessage.child("senderID").getValue(String::class.java), null , snapMessage.child("timeStamp").getValue(Long::class.java)!!)
                    Log.d("Msg",message.toString())
                    messages.add(message)
                }
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        reference.child("Presence").child(receiverUid!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                   val status = snapshot.getValue(String::class.java)
                    if (status == "offline"){
                        binding.imvOnline.visibility = View.GONE
                        //sacar el online del texto
                    }else{
                        binding.imvOnline.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })



        binding.btnSendMessage.setOnClickListener {
            if(binding.txpSendMessage.text.isNotEmpty()){
                val messageTxt:String = binding.txpSendMessage.text.toString()
                val date = Date()
                val message = Message (null, messageTxt, senderUid,null,date.time)
                binding.txpSendMessage.setText("")
                val randomKey = reference.push().key
                val lastMsgObj = HashMap<String,Any>()
                lastMsgObj["lastMsg"] = message.message!!
                lastMsgObj["lastMsgTime"] = date.time

                reference.child("chats").child(senderRoom!!).updateChildren(lastMsgObj)
                reference.child("chats").child(receiverRoom!!).updateChildren(lastMsgObj)
                reference.child("chats").child(senderRoom!!).child("messages").child(randomKey!!).setValue(message).addOnSuccessListener {
                    reference.child("chats").child(receiverRoom!!).child("messages").child(randomKey).setValue(message).addOnSuccessListener {

                    }
                }
            }
        }
        binding.imvAttachImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,25)
        }
        val handler = Handler()
        binding.txpSendMessage.addTextChangedListener (object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                reference.child("Presence").child(senderUid!!).setValue("Escribiendo...")
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(userStoppedTyping,1000)
            }
            var userStoppedTyping = Runnable {
                reference.child("Presence").child(senderUid!!).setValue("Online")
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 25&& data!=null && data.data !=null){
            val selectedImage = data.data
            val calendar = Calendar.getInstance()
            val refence = storageReference.child("chats").child(calendar.timeInMillis.toString()+"")
            dialog!!.show()
            refence.putFile(selectedImage!!).addOnCompleteListener{ task ->
                dialog!!.dismiss()
                if(task.isSuccessful){
                    refence.downloadUrl.addOnSuccessListener { uri->
                        val filePath = uri.toString()
                        val messageTxt = binding.txpSendMessage.text.toString()
                        val date = Date()
                        val message = Message(messageTxt,"photo",senderUid,filePath,date.time)
                        binding.txpSendMessage.setText("")
                        val randomkey = reference.push().key
                        val lastMsgObj = HashMap<String,Any>()
                        lastMsgObj["lastMsg"] = message.message!!
                        lastMsgObj["lastMsgTime"] = date.time
                        reference.child("chats").updateChildren(lastMsgObj)
                        reference.child("chats").child(receiverRoom!!).updateChildren(lastMsgObj)
                        reference.child("chats").child(senderRoom!!).child("messages").child(randomkey!!).setValue(message).addOnSuccessListener {
                            reference.child("chats").child(receiverRoom!!).child("messages").child(randomkey).setValue(message).addOnSuccessListener {  }
                        }
                    }
                }

            }
        }
    }
    override fun onResume() {
        super.onResume()
        val currentID = FirebaseAuth.getInstance().uid
        reference.child("Presence").child(currentID!!).setValue("Online")
    }
    override fun onPause() {
        super.onPause()
        val currentID = FirebaseAuth.getInstance().uid
        reference.child("Presence").child(currentID!!).setValue("Offline")
    }

    override fun onDestroy() {
        super.onDestroy()
        val currentID = FirebaseAuth.getInstance().uid
        reference.child("Presence").child(currentID!!).setValue("Offline")
    }
}