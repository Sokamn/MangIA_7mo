package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.settlet.mangia.databinding.ActivityMrecipeStep3Binding

class MRecipeStep3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMrecipeStep3Binding
    private var isMultiImages:Boolean = false
    private  var listImages = mutableListOf<String>()
    private lateinit var  uniqueImage:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        isMultiImages = getIntent().getBooleanExtra("isMultiImages",false)
        if (isMultiImages){
            for (i in 1..getIntent().getIntExtra("cant",0)){
                listImages.add(getIntent().getStringExtra("image$i")!!)
            }
            Glide.with(this)
                .load(listImages.first().toUri())
                .fitCenter()
                .into(binding.imvRecipePhotoMR3)
        }else{
            uniqueImage = getIntent().getStringExtra("uniqueImage")!!
            Glide.with(this)
                .load(uniqueImage.toUri())
                .fitCenter()
                .into(binding.imvRecipePhotoMR3)
        }

        //https://www.istockphoto.com/es/foto/de-pasta-italiana-verter-sobre-fondo-blanco-gm467084686-60661934
    }
}