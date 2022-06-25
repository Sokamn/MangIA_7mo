package com.settlet.mangia.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.settlet.mangia.R
import com.settlet.mangia.Model.Step
import com.settlet.mangia.ViewHolder.StepViewHolder

class StepAdapter(val stepList:List<Step>):RecyclerView.Adapter<StepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StepViewHolder(layoutInflater.inflate(R.layout.row_step_mr, parent, false))
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val item = stepList[position]
        val isExpandable:Boolean = item.expandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.vwBottomLine.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.vwTopLine.visibility = if (isExpandable) View.INVISIBLE else View.VISIBLE
        holder.imvChangeOptImage.visibility = if (isExpandable) View.GONE else View.VISIBLE
        holder.imvCloseImage.visibility = if (isExpandable) View.GONE else View.VISIBLE
        holder.imvOptionalImage.visibility = if (isExpandable) View.GONE else View.VISIBLE
        holder.imgExpand.setImageResource(if (isExpandable) R.drawable.ic_unity_collapse else R.drawable.ic_unity_expand)

        holder.expand.setOnClickListener{
            item.expandable = !item.expandable
            notifyItemChanged(position)
        }
        holder.render(item)
    }

    override fun getItemCount(): Int = stepList.size

}
