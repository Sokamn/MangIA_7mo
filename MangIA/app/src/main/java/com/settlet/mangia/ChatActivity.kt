package com.settlet.mangia

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.protobuf.Value
import com.settlet.mangia.Adapter.MessageAdapter
import com.settlet.mangia.Model.Message
import com.settlet.mangia.databinding.ActivityChatBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

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
    private lateinit var seenListener: ValueEventListener

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

        seenMessage()
        reference.child("Presence").child(receiverUid.toString()).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var status = snapshot.getValue(String::class.java)
                binding.txvStatus.text = status
                Log.d("status",status.toString())
                when(status.toString()){
                    "Escribiendo..."->{
                        binding.imvOnline.visibility = View.VISIBLE
                    }
                    "Online"->{
                        binding.imvOnline.visibility = View.VISIBLE
                    }
                    "Offline"->{
                        binding.imvOnline.visibility = View.GONE
                    }
                    else->{

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

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
                    val message = Message(snapMessage.key,snapMessage.child("message").getValue(String::class.java),snapMessage.child("senderID").getValue(String::class.java), snapMessage.child("imageUrl").getValue(String::class.java) , snapMessage.child("timeStamp").getValue(Long::class.java)!!,snapMessage.child("hour").getValue(String::class.java)!!, snapMessage.child("seen").getValue(Boolean::class.java)!!)
                    if(message.message!=this@ChatActivity.getString(R.string.imageSent)){
                        message.imageUrl = null
                    }
                    Log.d("Msg",message.toString())
                    messages.add(message)
                    binding.rcvMessagesChat.layoutManager?.scrollToPosition(messages.size-1)
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
                val localTime = LocalTime.now()
                val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                val hour = localTime.format(dateTimeFormatter)
                val message = Message(null, messageTxt, senderUid, null, date.time, hour, false)
                binding.txpSendMessage.setText("")
                val randomKey = reference.push().key
                val lastMsgObj = HashMap<String,Any>()
                lastMsgObj["lastMsg"] = message.message!!
                lastMsgObj["hour"] = message.hour!!
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
        binding.txpSendMessage.setOnClickListener {
            binding.rcvMessagesChat.layoutManager?.scrollToPosition(messages.size-1)
        }
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

    private fun seenMessage(){
        seenListener = reference.child("chats").child(receiverRoom!!).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("messages").children.forEach {
                    if(it.child("senderID").getValue(String::class.java)==receiverUid){
                        reference.child("chats").child(receiverRoom!!).child("messages").child(it.key.toString()).child("seen").setValue(true)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 25&& data!=null && data.data !=null){
            val selectedImage = data.data
            val path = Calendar.getInstance().timeInMillis.toString()
            val localTime = LocalTime.now()
            val dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
            val hour = localTime.format(dateTimeFormatter)
            val refence = storageReference.child("chats").child(path)
            dialog!!.show()
            refence.putFile(selectedImage!!).addOnCompleteListener{ task ->
                dialog!!.dismiss()
                if(task.isSuccessful){
                    refence.downloadUrl.addOnSuccessListener { uri->
                        val date = Date()
                        val message = Message(null,this.getString(R.string.imageSent),senderUid,path,date.time,hour,false)
                        binding.txpSendMessage.setText("")
                        val randomkey = reference.push().key
                        val lastMsgObj = HashMap<String,Any>()
                        lastMsgObj["lastMsg"] = message.message!!
                        lastMsgObj["hour"] = message.hour!!
                        lastMsgObj["lastMsgTime"] = date.time
                        reference.child("chats").child(senderRoom!!).updateChildren(lastMsgObj)
                        reference.child("chats").child(receiverRoom!!).updateChildren(lastMsgObj)
                        reference.child("chats").child(senderRoom!!).child("messages").child(randomkey!!).setValue(message).addOnSuccessListener {

                        }
                        reference.child("chats").child(receiverRoom!!).child("messages").child(randomkey).setValue(message).addOnSuccessListener {

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
        reference.child("chats").child(receiverRoom!!).removeEventListener(seenListener)
        reference.child("Presence").child(currentID!!).setValue("Offline")
    }

    override fun onDestroy() {
        super.onDestroy()
        val currentID = FirebaseAuth.getInstance().uid
        reference.child("chats").child(receiverRoom!!).removeEventListener(seenListener)
        reference.child("Presence").child(currentID!!).setValue("Offline")
    }
}