package com.settlet.mangia

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.settlet.mangia.databinding.ActivityMrecipeStep2Binding

class MRecipeStep2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMrecipeStep2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val arrayAdapterLIngredientNames = ArrayAdapter<String>(this, R.layout.simple_list_item_1, IngredientProvider.ingredientListN)
        binding.txpSearchMRS2.setAdapter(arrayAdapterLIngredientNames)

    }
}