package com.settlet.mangia

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.settlet.mangia.databinding.RowStepMrBinding

class StepViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding = RowStepMrBinding.bind(view)
    var expandableLayout = binding.cstExpandableLayout

    fun render(step: Step){

    }
}
