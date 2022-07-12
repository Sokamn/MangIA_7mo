package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.Model.Step
import com.settlet.mangia.databinding.ActivityMrecipeStep3Binding

class MRecipeStep3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMrecipeStep3Binding
    private var isMultiImages:Boolean = false
    private  var listImages = mutableListOf<String>()
    private var listIngredient = mutableListOf<Ingredient>()
    private var listStep = mutableListOf<Step>()
    private var quantIngred = 0
    private var quantStep = 0
    private lateinit var  uniqueImage:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        isMultiImages = intent.getBooleanExtra("isMultiImages",false)
        if (isMultiImages){
            for (i in 1..intent.getIntExtra("cant",0)){
                listImages.add(intent.getStringExtra("image$i")!!)
            }
            Glide.with(this)
                .load(listImages.first().toUri())
                .fitCenter()
                .into(binding.imvRecipePhotoMR3)
        }else{
            uniqueImage = intent.getStringExtra("uniqueImage")!!
            Glide.with(this)
                .load(uniqueImage.toUri())
                .fitCenter()
                .into(binding.imvRecipePhotoMR3)
        }
        quantIngred = intent.getIntExtra("cantIngredients",0)
        quantStep = intent.getIntExtra("cantSteps",0)
        for(i in 1..quantIngred){
            listIngredient.add(Ingredient(intent.getStringExtra("ingr$i")!!,intent.getStringExtra("unity$i")!!,0F,intent.getIntExtra("ingr$i",0),null))
        }
        for(i in 1..quantStep){
            listStep.add(Step(i,intent.getStringExtra("step$i")!!))
        }

        binding.chbVegan.setOnClickListener {
            if(binding.chbVegan.isChecked){
                binding.chbVegetarian.isChecked = true
                binding.chbVegetarian.isEnabled = false
            }else{
                binding.chbVegetarian.isChecked = false
                binding.chbVegetarian.isEnabled = true
            }
        }
        binding.chbVegetarian.setOnClickListener {
            if(binding.chbVegetarian.isChecked){
                binding.chbVegan.isChecked = false
                binding.chbVegan.isEnabled = false
            }else{
                binding.chbVegan.isEnabled = true
            }
        }
        //https://www.istockphoto.com/es/foto/de-pasta-italiana-verter-sobre-fondo-blanco-gm467084686-60661934
    }
}