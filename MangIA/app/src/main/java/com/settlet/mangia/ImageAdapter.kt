package com.settlet.mangia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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
            val imageAdded = act.findViewById<ImageView>(R.id.imvImageAdded)
            Glide.with(context)
                .load(currentImage.imagePath)
                .into(imageAdded)
        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

}