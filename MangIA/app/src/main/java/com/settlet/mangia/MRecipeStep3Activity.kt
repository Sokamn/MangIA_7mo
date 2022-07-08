package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.settlet.mangia.databinding.ActivityMrecipeStep3Binding

class MRecipeStep3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMrecipeStep3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        //https://www.istockphoto.com/es/foto/de-pasta-italiana-verter-sobre-fondo-blanco-gm467084686-60661934
    }
}