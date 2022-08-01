package com.settlet.mangia.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.UserRateViewHolder

class UserRateAdapter : ListAdapter<Array<String>, UserRateViewHolder>(DiffCallBack){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRateViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_ingredient_recycler,parent,false)
        return UserRateViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserRateViewHolder, position: Int) {
        val item = getItem(position)
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