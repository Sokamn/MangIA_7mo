package com.settlet.mangia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StepAdapter(val stepList:List<Step>):RecyclerView.Adapter<StepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StepViewHolder(layoutInflater.inflate(R.layout.row_step_mr, parent, false))
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val item = stepList[position]
        val isExpandable:Boolean = item.expandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.expandableLayout.setOnClickListener {
            item.expandable = !item.expandable
            notifyItemChanged(position)
        }
        holder.render(item)
    }

    override fun getItemCount(): Int = stepList.size

}