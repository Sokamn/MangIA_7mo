package com.settlet.mangia.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.settlet.mangia.Model.Message
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.MessageViewHolder

class MessageAdapter : ListAdapter<Message, MessageViewHolder>(DiffCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_message,parent,false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Message>(){
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.userID == newItem.userID
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }
}