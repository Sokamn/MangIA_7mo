package com.settlet.mangia.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.settlet.mangia.Model.Message
import com.settlet.mangia.R
import com.settlet.mangia.databinding.DeleteLayoutBinding
import com.settlet.mangia.databinding.RowReceiveMsgBinding
import com.settlet.mangia.databinding.RowSentMsgBinding

class MessageAdapter(var context: Context, senderRoom:String, receiverRoom:String) : ListAdapter<Message, RecyclerView.ViewHolder?>(DiffCallBack){

    lateinit var messages: MutableList<Message>
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
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
        val message = messages[position]
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
            if(item.message.equals("photo")){
                viewHolder.binding.imvOImage.visibility = View.VISIBLE
                viewHolder.binding.txvTextSent.visibility = View.INVISIBLE
                viewHolder.binding.llLinearSent.visibility = View.INVISIBLE
                Glide.with(context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(viewHolder.binding.imvOImage)
            }
            viewHolder.binding.txvTextSent.text = item.message
            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout,null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context).setTitle("Eliminar Mensaje").setView(binding.root).create()
                binding.txvEveryone.setOnClickListener {
                    item.message = "Este mensaje ha sido eliminado para todos."
                    item.messageID?.let{ it1->
                        reference.child("chats").child(senderRoom).child("messages").child(it1).setValue(item)
                    }
                    item.messageID.let{ it1->
                        reference.child("chats").child(receiverRoom).child("messages").child(it1!!).setValue(item)
                    }
                    dialog.dismiss()
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
            if(item.message=="photo"){
                viewHolder.binding.imvOImage.visibility = View.VISIBLE
                viewHolder.binding.txvTextReceived.visibility = View.INVISIBLE
                viewHolder.binding.llLinearReceived.visibility = View.INVISIBLE
                Glide.with(context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(viewHolder.binding.imvOImage)
            }
            viewHolder.binding.txvTextReceived.text = item.message
            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout,null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context).setTitle("Eliminar Mensaje").setView(binding.root).create()
                binding.txvEveryone.setOnClickListener {
                    item.message = "Este mensaje ha sido eliminado para todos."
                    item.messageID?.let{ it1->
                        reference.child("chats").child(senderRoom).child("messages").child(it1).setValue(item)
                    }
                    item.messageID.let{ it1->
                        reference.child("chats").child(receiverRoom).child("messages").child(it1!!).setValue(item)
                    }
                    dialog.dismiss()
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
    init{
        if(this.currentList != null){
            this.messages = currentList
        }
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