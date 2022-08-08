package com.settlet.mangia.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.settlet.mangia.Model.AnswerViewHolder
import com.settlet.mangia.Model.Comment
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.CommentViewHolder

class AnswerAdapter : ListAdapter<Comment, AnswerViewHolder>(DiffCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_comment_answer,parent,false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.answerID == newItem.answerID
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
}