package com.settlet.mangia

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_mrecipe_step1.*
import java.io.File

class ImageAdapter (private var context: Context, private var imagesList:ArrayList<Image>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
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
            val act = context as MRecipeStep1Activity
            val a: File = File(currentImage.imagePath!!)
            val inputUri = Uri.fromFile(a)
            val outputUri = File("image/","croppedImage.jpg").toUri()
            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f,5f)
                .withMaxResultSize(1080,1080)
            val intent = uCrop.getIntent(context)
            UCrop.getOutput(intent!!)!!
            act.imvImageAdded.setImageURI(outputUri)

        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

}