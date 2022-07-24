package com.settlet.mangia.Adapter


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import com.settlet.mangia.Model.Step
import com.settlet.mangia.R
import com.settlet.mangia.ViewHolder.StepViewHolder
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.settlet.mangia.MRecipeStep2Activity
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_mrecipe_step2.*
import java.io.File

class StepAdapter(con:Context) : ListAdapter<Step, StepViewHolder>(DiffCallBack){
    val context = con as MRecipeStep2Activity
    var imgAdded:ImageView? = null
    var txvOptionalImage:TextView? = null
    var imgRemoveImg:ImageView? = null
    var imgChangeImg:ImageView? = null
    var externalStep:Step? = null
    var i = 0

    private val getContent = context.registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri!=null)
        {
            val inputUri = uri
            while(File(context.filesDir,"croppedImage${i}.jpg").exists())
            {
                i++
            }
            val outputUri = File(context.filesDir,"croppedImage${i}.jpg").toUri()
            val listUri = listOf<Uri>(inputUri,outputUri)
            cropImage.launch(listUri)
        }
        else{
            context.listStepRecipe.forEach {
                if(it.nStep == externalStep!!.nStep){
                    if(it.optionalImage==null){
                        txvOptionalImage!!.visibility = View.VISIBLE
                        imgAdded!!.visibility = View.GONE
                        imgChangeImg!!.visibility = View.GONE
                        imgRemoveImg!!.visibility = View.GONE
                    }
                }
            }
            Toast.makeText(context.baseContext,"No has seleccionado ninguna imagen.", Toast.LENGTH_SHORT).show()
        }
    }
    private val uCropContract = object: ActivityResultContract<List<Uri>, Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]
            Log.d("URI","${input[0]} ! ${input[1]}")

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f,5f)
                .withMaxResultSize(1080,1080)
            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if(intent!=null)
            {
                return UCrop.getOutput(intent)!!
            }
            else
            {
                return null
            }
        }
    }
    private val cropImage = context.registerForActivityResult(uCropContract){ uri ->
        if (uri!=null)
        {
            imgAdded!!.setImageURI(null)
            context.listStepRecipe.forEach {
                if(it.nStep == externalStep!!.nStep){
                    it.optionalImage = uri
                }
            }
            Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(imgAdded!!)
            txvOptionalImage!!.visibility = View.GONE
            imgAdded!!.visibility = View.VISIBLE
            imgRemoveImg!!.visibility = View.VISIBLE
            imgChangeImg!!.visibility = View.VISIBLE
            context.rcvStepsMR2.adapter!!.notifyDataSetChanged()
        }
        else{
            context.listStepRecipe.forEach {
                if(it.nStep == externalStep!!.nStep){
                    if(it.optionalImage==null){
                        txvOptionalImage!!.visibility = View.VISIBLE
                        imgAdded!!.visibility = View.GONE
                        imgChangeImg!!.visibility = View.GONE
                        imgRemoveImg!!.visibility = View.GONE
                    }
                }
            }
            Toast.makeText(context.baseContext,"No has terminado de recortar una imagen.", Toast.LENGTH_SHORT).show()
        }
    }

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
        if (isExpandable){
            if(item.optionalImage!=null){
                holder.binding.txvAddOptionalmage.visibility = View.GONE
                holder.binding.imvImageStepRS.visibility = View.VISIBLE
                holder.binding.imvRemoveImageRSMR.visibility = View.VISIBLE
                holder.binding.imvChangeImage.visibility = View.VISIBLE
            }else{
                holder.binding.txvAddOptionalmage.visibility = View.VISIBLE
                holder.binding.imvImageStepRS.visibility = View.GONE
                holder.binding.imvRemoveImageRSMR.visibility = View.GONE
                holder.binding.imvChangeImage.visibility = View.GONE
            }
        }else{
            holder.binding.txvAddOptionalmage.visibility = View.GONE
            holder.binding.imvImageStepRS.visibility = View.GONE
            holder.binding.imvRemoveImageRSMR.visibility = View.GONE
            holder.binding.imvChangeImage.visibility = View.GONE
        }

        holder.expand.setOnClickListener{
            item.expandable = !item.expandable
            notifyItemChanged(position)
        }
        holder.binding.txvAddOptionalmage.setOnClickListener {
            externalStep = item
            imgAdded = holder.binding.imvImageStepRS
            txvOptionalImage = holder.binding.txvAddOptionalmage
            imgChangeImg = holder.binding.imvChangeImage
            imgRemoveImg = holder.binding.imvRemoveImageRSMR
            getContent.launch("image/*")
        }
        holder.binding.imvChangeImage.setOnClickListener {
            externalStep = item
            imgAdded = holder.binding.imvImageStepRS
            txvOptionalImage = holder.binding.txvAddOptionalmage
            imgChangeImg = holder.binding.imvChangeImage
            imgRemoveImg = holder.binding.imvRemoveImageRSMR
            getContent.launch("image/*")
        }
        holder.binding.imvRemoveImageRSMR.setOnClickListener {
            holder.binding.txvAddOptionalmage.visibility = View.VISIBLE
            holder.binding.imvImageStepRS.visibility = View.GONE
            holder.binding.imvRemoveImageRSMR.visibility = View.GONE
            holder.binding.imvChangeImage.visibility = View.GONE
            item.optionalImage = null
        }
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