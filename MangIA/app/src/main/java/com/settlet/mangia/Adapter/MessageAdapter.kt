package com.settlet.mangia.Adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.Message
import com.settlet.mangia.R
import com.settlet.mangia.databinding.DeleteLayoutBinding
import com.settlet.mangia.databinding.RowReceiveMsgBinding
import com.settlet.mangia.databinding.RowSentMsgBinding
import kotlinx.android.synthetic.main.row_receive_msg.view.*

class MessageAdapter(var context: Context, senderRoom:String, receiverRoom:String) : ListAdapter<Message, RecyclerView.ViewHolder?>(DiffCallBack){

    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    private val storageReference = FirebaseStorage.getInstance().reference
    private val reference = FirebaseDatabase.getInstance().reference
    private val senderRoom = senderRoom
    private val receiverRoom = receiverRoom

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == ITEM_SENT){
            val view = LayoutInflater.from(context).inflate(R.layout.row_sent_msg,parent,false)
            SentMsgHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.row_receive_msg,parent,false)
            ReceiveMsgHolder(view)
        }
    }
    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if(FirebaseAuth.getInstance().uid == message.senderID){
            ITEM_SENT
        }else{
            ITEM_RECEIVE
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if(holder.javaClass == SentMsgHolder::class.java){
            val viewHolder = holder as SentMsgHolder
            if(item.message.equals(context.getString(R.string.imageSent))){
                viewHolder.binding.imvOImage.visibility = View.VISIBLE
                viewHolder.binding.txvTextSent.visibility = View.GONE
                viewHolder.binding.llLinearSent.visibility = View.GONE
                storageReference.child("chats").child(item.imageUrl.toString()).downloadUrl.addOnSuccessListener { result ->
                    Glide.with(context)
                        .load(result)
                        .placeholder(R.drawable.image_placeholder)
                        .into(viewHolder.binding.imvOImage)
                }
            }
            if(item.imageUrl == null){
                viewHolder.binding.imvOImage.visibility = View.GONE
                viewHolder.binding.txvTextSent.visibility = View.VISIBLE
                viewHolder.binding.llLinearSent.visibility = View.VISIBLE
            }
            viewHolder.binding.txvTextSent.text = item.message
            viewHolder.binding.txvHourSent.text = item.hour
            if(item.seen) viewHolder.binding.imvSeenMsg.setImageResource(R.drawable.ic_seen) else viewHolder.binding.imvSeenMsg.setImageResource(R.drawable.ic_unseen)
            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout,null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context).setTitle("Eliminar Mensaje").setView(binding.root).create()
                binding.txvEveryone.setOnClickListener {
                    reference.child("chats").child(senderRoom).child("lastMsg").get().addOnSuccessListener {
                        if (it.value.toString()==item.message){
                            reference.child("chats").child(senderRoom).child("lastMsg").setValue(context.getString(R.string.eliminatedMessage))
                            item.message = context.getString(R.string.eliminatedMessage)
                            item.messageID?.let{ it1->
                                reference.child("chats").child(senderRoom).child("messages").child(it1).setValue(item)
                            }
                            item.messageID.let{ it1->
                                reference.child("chats").child(receiverRoom).child("messages").child(it1!!).setValue(item)
                            }
                            dialog.dismiss()
                        }
                        else{
                            item.message = context.getString(R.string.eliminatedMessage)
                            item.messageID?.let{ it1->
                                reference.child("chats").child(senderRoom).child("messages").child(it1).setValue(item)
                            }
                            item.messageID.let{ it1->
                                reference.child("chats").child(receiverRoom).child("messages").child(it1!!).setValue(item)
                            }
                            dialog.dismiss()
                        }
                    }
                }
                binding.txvDeleteForMe.setOnClickListener {
                    item.messageID.let{ it1->
                        reference.child("chats").child(senderRoom).child("messages").child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }
                binding.txvCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
                false
            }
        }else{
            val viewHolder = holder as ReceiveMsgHolder
            if(item.message.equals(context.getString(R.string.imageSent))){
                viewHolder.binding.imvOImage.visibility = View.VISIBLE
                viewHolder.binding.txvTextReceived.visibility = View.GONE
                viewHolder.binding.llLinearReceived.visibility = View.GONE
                storageReference.child("chats").child(item.imageUrl.toString()).downloadUrl.addOnSuccessListener { result ->
                    Glide.with(context)
                        .load(result)
                        .placeholder(R.drawable.image_placeholder)
                        .into(viewHolder.binding.imvOImage)
                }
            }
            if(item.imageUrl == null){
                viewHolder.binding.imvOImage.visibility = View.GONE
                viewHolder.binding.txvTextReceived.visibility = View.VISIBLE
                viewHolder.binding.llLinearReceived.visibility = View.VISIBLE
            }
            viewHolder.binding.txvTextReceived.text = item.message
            viewHolder.binding.txvHourReceive.text = item.hour
            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout,null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context).setTitle("Eliminar Mensaje").setView(binding.root).create()
                binding.txvEveryone.setOnClickListener {
                    reference.child("chats").child(senderRoom).child("lastMsg").get().addOnSuccessListener {
                        if (it.value.toString()==item.message){
                            reference.child("chats").child(senderRoom).child("lastMsg").setValue(context.getString(R.string.eliminatedMessage))
                            item.message = context.getString(R.string.eliminatedMessage)
                            item.messageID?.let{ it1->
                                reference.child("chats").child(senderRoom).child("messages").child(it1).setValue(item)
                            }
                            item.messageID.let{ it1->
                                reference.child("chats").child(receiverRoom).child("messages").child(it1!!).setValue(item)
                            }
                            dialog.dismiss()
                        }
                        else{
                            item.message = context.getString(R.string.eliminatedMessage)
                            item.messageID?.let{ it1->
                                reference.child("chats").child(senderRoom).child("messages").child(it1).setValue(item)
                            }
                            item.messageID.let{ it1->
                                reference.child("chats").child(receiverRoom).child("messages").child(it1!!).setValue(item)
                            }
                            dialog.dismiss()
                        }
                    }
                }
                binding.txvDeleteForMe.setOnClickListener {
                    item.messageID.let{ it1->
                        reference.child("chats").child(senderRoom).child("messages").child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }
                binding.txvCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
                false
            }
        }
    }

    inner class SentMsgHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var binding:RowSentMsgBinding = RowSentMsgBinding.bind(itemView)
    }
    inner class ReceiveMsgHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var binding:RowReceiveMsgBinding = RowReceiveMsgBinding.bind(itemView)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Message>(){
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.messageID == newItem.messageID
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }
}