package com.settlet.mangia.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.ProfileActivity
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.UserRateViewHolder

class UserRateAdapter : ListAdapter<Array<String>, UserRateViewHolder>(DiffCallBack){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRateViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_user_rate,parent,false)
        return UserRateViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserRateViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener{
            val editor = holder.itemView.context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileEmail", item[0])
            editor.apply()
            holder.itemView.context.startActivity(Intent(holder.itemView.context,ProfileActivity::class.java))
        }
        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Array<String>>(){
        override fun areItemsTheSame(oldItem: Array<String>, newItem: Array<String>): Boolean {
            return oldItem[0] == newItem[0]
        }

        override fun areContentsTheSame(oldItem: Array<String>, newItem: Array<String>): Boolean {
            return oldItem.contentEquals(newItem)
        }
    }
}