package com.settlet.mangia.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.settlet.mangia.MRecipeStep2Activity
import com.settlet.mangia.R
import com.settlet.mangia.Model.Step
import com.settlet.mangia.ViewHolder.StepViewHolder
import com.yalantis.ucrop.UCrop
import java.io.File

lateinit var  holderExt: StepViewHolder

class MyLifecycleObserver(private val registry : ActivityResultRegistry)
    : DefaultLifecycleObserver {
    lateinit var getContent : ActivityResultLauncher<String>
    lateinit var uCropContract: ActivityResultContract<List<Uri>, Uri>
    lateinit var cropImage: ActivityResultLauncher<List<Uri>>
    var finishedListener:Boolean = false
    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("key", owner, ActivityResultContracts.GetContent()) { uri ->
            if(uri!=null)
            {
                val inputUri = uri
                val outputUri = File(holderExt.context.filesDir,"croppedImage.jpg").toUri()
                val listUri = listOf<Uri>(inputUri,outputUri)
                cropImage.launch(listUri)
            }
            else{
                Toast.makeText(holderExt.context.baseContext,"No has seleccionado ninguna imagen.", Toast.LENGTH_SHORT).show()
                finishedListener = false
            }
        }
        uCropContract = object: ActivityResultContract<List<Uri>, Uri>(){
            override fun createIntent(context: Context, input: List<Uri>): Intent {
                val inputUri = input[0]
                val outputUri = input[1]

                val uCrop = UCrop.of(inputUri, outputUri)
                    .withAspectRatio(5f,5f)
                    .withMaxResultSize(1080,1080)
                return uCrop.getIntent(context)
            }


            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return if(intent!=null) {
                    UCrop.getOutput(intent)!!
                } else {
                    null
                }
            }
        }
        cropImage = registry.register("key", owner, uCropContract){ uri ->
            finishedListener = if (uri!=null) {
                holderExt.binding.imvImageStepRS.visibility = View.VISIBLE
                holderExt.binding.imvChangeImage.visibility = View.VISIBLE
                holderExt.binding.imvRemoveImageRSMR.visibility = View.VISIBLE
                Glide.with(holderExt.context)
                    .load(uri)
                    .centerCrop()
                    .into(holderExt.imvOptionalImage)
                true
            } else{
                Toast.makeText(holderExt.context.baseContext,"No has terminado de recortar una imagen.", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }
    fun selectImage() {
        getContent.launch("image/*")
    }
}

class StepAdapter(val stepList:List<Step>):RecyclerView.Adapter<StepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StepViewHolder(layoutInflater.inflate(R.layout.row_step_mr, parent, false))
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holderExt = holder
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
        holder.binding.txvAddOptionalmage.setOnClickListener{
            holder.context.observer.selectImage()
        }
        holder.render(item)
    }

    override fun getItemCount(): Int = stepList.size

}
