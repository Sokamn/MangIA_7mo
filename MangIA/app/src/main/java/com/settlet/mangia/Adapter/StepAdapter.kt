package com.settlet.mangia.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.settlet.mangia.Model.Step
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.StepViewHolder
import androidx.recyclerview.widget.ListAdapter

class StepAdapter : ListAdapter<Step, StepViewHolder>(DiffCallBack){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_step_mr,parent,false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val item = getItem(position)
        val isExpandable: Boolean = item.expandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.vwBottomLine.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.vwTopLine.visibility = if (isExpandable) View.INVISIBLE else View.VISIBLE
        holder.binding.txpDescriptionMR.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.imvChangeOptImage.visibility = if (isExpandable) View.GONE else View.VISIBLE
        holder.imvCloseImage.visibility = if (isExpandable) View.GONE else View.VISIBLE
        holder.imvOptionalImage.visibility = if (isExpandable) View.GONE else View.VISIBLE
        holder.imgExpand.setImageResource(if (isExpandable) R.drawable.ic_unity_collapse else R.drawable.ic_unity_expand)

        holder.expand.setOnClickListener{
            item.expandable = !item.expandable
            notifyItemChanged(position)
        }
        /*holder.binding.txvAddOptionalmage.setOnClickListener{
            holder.context.observer.selectImage()
        }*/
        holder.render(item)
    }
    
    companion object DiffCallBack: DiffUtil.ItemCallback<Step>(){
        override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem.nStep == newItem.nStep
        }

        override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem == newItem
        }

    }
}