package com.settlet.mangia.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.settlet.mangia.MRecipeStep1Activity
import com.settlet.mangia.R
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(imageArray: MutableList<String>,isUri:Boolean): SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    private var sliderList: MutableList<String> = imageArray
    var uri = isUri
    class SliderAdapterVH(itemView: View?): SliderViewAdapter.ViewHolder(itemView) {
        var imageView: ImageView = itemView!!.findViewById(R.id.myimage)
    }

    override fun getCount(): Int = sliderList.size

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.slider_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {
        if (viewHolder != null) {
            val c = viewHolder.itemView.context
            Log.d("URI" ,sliderList[position])
            if(uri) {
                if (sliderList[position] == "doesntexist") {
                    Glide.with(c)
                        .load(R.drawable.profile_picture)
                        .centerCrop()
                        .into(viewHolder.imageView)
                } else {
                    Glide.with(c)
                        .load(sliderList[position].toUri())
                        .fitCenter()
                        .into(viewHolder.imageView)
                }
            }
            else{
                Glide.with(c)
                    .load(sliderList[position])
                    .fitCenter()
                    .into(viewHolder.imageView)
            }
        }
    }
}