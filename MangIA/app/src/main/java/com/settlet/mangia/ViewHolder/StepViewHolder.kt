package com.settlet.mangia.ViewHolder

import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.settlet.mangia.MRecipeStep2Activity
import com.settlet.mangia.Model.Step
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
    val imvOptionalImage = binding.imvImageStepRS
    val imvChangeOptImage = binding.imvChangeImage
    val imvCloseImage = binding.imvRemoveImageRSMR

    fun render(step: Step){
       binding.txpDescriptionMR.setText(step.sDescription)

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

        binding.imvRemoveImageRSMR.setOnClickListener {
            binding.imvImageStepRS.visibility = View.GONE
            binding.imvChangeImage.visibility = View.GONE
            binding.imvRemoveImageRSMR.visibility = View.GONE
            binding.txvAddOptionalmage.visibility = View.VISIBLE
            step.optionalImage = null
        }

        binding.imbApplyChanges.setOnClickListener {
            if(step.sDescription == binding.txpDescriptionMR.text.toString()){
                Toast.makeText(context.baseContext,"No hay cambios en el paso ${step.nStep}",Toast.LENGTH_SHORT).show()
            }else{
                step.sDescription = binding.txpDescriptionMR.text.toString()
                context.rcvStepsMR2.adapter!!.notifyDataSetChanged()
                binding.imvAlert.visibility = View.GONE
                Toast.makeText(context.baseContext,"Cambios aplicados en el paso ${step.nStep}",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
