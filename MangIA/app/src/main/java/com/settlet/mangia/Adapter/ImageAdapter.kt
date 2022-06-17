package com.settlet.mangia.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.settlet.mangia.MRecipeStep1Activity
import com.settlet.mangia.Model.Image
import com.settlet.mangia.R
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_mrecipe_step1.*
import java.io.File

class ImageAdapter (private var context: Context, private var imagesList:ArrayList<Image>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    val con = context as MRecipeStep1Activity
    val getContent = con.registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        val inputUri = uri
        val outputUri = File(con.filesDir,"croppedImage.jpg").toUri()
        val listUri = listOf<Uri>(inputUri,outputUri)
        cropImage.launch(listUri)
    }
    val uCropContract = object: ActivityResultContract<List<Uri>,Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]
            Log.d("URI","${input[0]} ! ${input[1]}")

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f,5f)
                .withMaxResultSize(1080,1080)
            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }
    }
    val cropImage = con.registerForActivityResult(uCropContract){ uri ->
        con.imvImageAdded.setImageURI(uri)
    }
    class ImageViewHolder (itemView: View):RecyclerView.ViewHolder(itemView) {
        var image:ImageView?=null
        init{
            image = itemView.findViewById(R.id.row_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_gallery_recycler,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = imagesList[position]
        Glide.with(context)
            .load(currentImage.imagePath)
            .apply(RequestOptions().centerCrop())
            .into(holder.image!!)

        holder.image?.setOnClickListener {
            /*val act = context as MRecipeStep1Activity
            val a: File = File(currentImage.imagePath!!)
            val inputUri = Uri.fromFile(a)
            val outputUri = File("image/","croppedImage.jpg").toUri()
            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f,5f)
                .withMaxResultSize(1080,1080)
            val intent = uCrop.getIntent(context)
            UCrop.getOutput(intent!!)!!
            act.imvImageAdded.setImageURI(outputUri)*/
            val con = holder.image!!.context as MRecipeStep1Activity
            val a: File = File(currentImage.imagePath!!)
            getContent.launch(currentImage.imagePath)

    }
}
    override fun getItemCount(): Int =imagesList.size
}