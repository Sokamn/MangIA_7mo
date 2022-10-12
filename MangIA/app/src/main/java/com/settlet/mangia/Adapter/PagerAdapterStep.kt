package com.settlet.mangia.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.Step
import com.settlet.mangia.R
import com.settlet.mangia.RecipeActivity
import com.settlet.mangia.databinding.VpStepItemBinding

class PagerAdapterStep : ListAdapter<Step, PagerAdapterStep.VPStepViewHolder>(DiffCallBack){
    class VPStepViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = VpStepItemBinding.bind(view)
        private val storageReference = FirebaseStorage.getInstance().reference

        fun render(step: Step){
            binding.txvStepTitleVSI.text = "Paso ${step.nStep}"
            if(step.optionalImage.isNullOrEmpty()){
                binding.imvOptionalImageVSI.visibility = View.GONE
            }else{
                val context = itemView.context as RecipeActivity
                if(!context.isDestroyed){
                    binding.imvOptionalImageVSI.visibility = View.VISIBLE
                    storageReference.child(step.optionalImage.toString()).downloadUrl.addOnSuccessListener { result ->
                        Glide.with(binding.imvOptionalImageVSI.context)
                            .load(result)
                            .into(binding.imvOptionalImageVSI)

                    }.addOnFailureListener {
                        Glide.with(binding.imvOptionalImageVSI.context)
                            .load(R.drawable.ic_load_ingredient)
                            .into(binding.imvOptionalImageVSI)
                    }
                }
            }
            binding.txvStepDescriptionVSI.text = step.sDescription
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VPStepViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.vp_step_item,parent,false)
        return VPStepViewHolder(view)
    }

    override fun onBindViewHolder(holder: VPStepViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Step>(){
        override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem.nStep == oldItem.nStep
        }

        override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem == newItem
        }

    }

}