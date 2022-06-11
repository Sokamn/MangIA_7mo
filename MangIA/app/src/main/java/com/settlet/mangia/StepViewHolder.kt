package com.settlet.mangia

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.settlet.mangia.databinding.RowStepMrBinding
import kotlinx.android.synthetic.main.activity_mrecipe_step2.*

class StepViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding = RowStepMrBinding.bind(view)
    var expandableLayout = binding.cstExpandableLayout
    var expand = binding.cstExpand
    var vwTopLine = binding.viewTopLine
    var vwBottomLine = binding.viewBottomLine
    var imgExpand = binding.imvExpand
    val context: MRecipeStep2Activity = binding.imvRemove.context as MRecipeStep2Activity

    fun render(step: Step){
        binding.txvStepNumber.text = "Paso ${step.nStep}"

        binding.imvRemove.setOnClickListener {

            context.listStepRecipe.remove(step)
            context.quantSteps = context.quantSteps - 1
            context.listStepRecipe.forEach {
                if(it.nStep>step.nStep)
                {
                    it.nStep--
                }
            }
            context.rcvStepsMR2.adapter!!.notifyDataSetChanged()
        }
    }
}
