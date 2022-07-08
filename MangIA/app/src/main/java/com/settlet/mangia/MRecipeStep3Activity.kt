package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.settlet.mangia.databinding.ActivityMrecipeStep3Binding

class MRecipeStep3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMrecipeStep3Binding
    private lateinit var bundle:Bundle
    private var isMultiImages:Boolean = false
    private lateinit var listImages:MutableList<String>
    private lateinit var  uniqueImage:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        bundle = intent.extras!!
        isMultiImages = bundle.getBoolean("isMultiImages")
        if (isMultiImages){
            for (i in 1..bundle.getString("cant")!!.toInt()){
                listImages.add(bundle.getString("image$i")!!)
            }
            Glide.with(this)
                .load(listImages.first().toUri())
                .fitCenter()
                .into(binding.imvRecipePhotoMR3)
        }else{
            uniqueImage = bundle.getString("uniqueImage")!!
            Glide.with(this)
                .load(uniqueImage.toUri())
                .fitCenter()
                .into(binding.imvRecipePhotoMR3)
        }

        //https://www.istockphoto.com/es/foto/de-pasta-italiana-verter-sobre-fondo-blanco-gm467084686-60661934
    }
}