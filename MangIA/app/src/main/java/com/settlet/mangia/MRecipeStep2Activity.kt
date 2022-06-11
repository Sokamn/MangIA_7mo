package com.settlet.mangia

import android.R
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.settlet.mangia.databinding.ActivityMrecipeStep2Binding

class MRecipeStep2Activity : AppCompatActivity() {
    internal val listIngredientRecipe = mutableListOf<Ingredient>()
    internal val listStepRecipe = mutableListOf<Step>()
    var quantSteps: Int = 0
    private lateinit var binding: ActivityMrecipeStep2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initRCVIngredients(listIngredientRecipe)
        initRCVSteps(listStepRecipe)
        val arrayAdapterLIngredientNames = ArrayAdapter<String>(this, R.layout.simple_list_item_1, IngredientProvider.ingredientListN)
        binding.txpSearchMRS2.setAdapter(arrayAdapterLIngredientNames)
        binding.txpSearchMRS2.setOnItemClickListener { parent, view, position, id ->
            IngredientProvider.ingredientListO.forEach {
                if(it.nombre == parent.getItemAtPosition(position)){
                    if(listIngredientRecipe.contains(it))
                    {
                        Toast.makeText(this,"Este ingrediente ya ha sido agregado.",Toast.LENGTH_SHORT).show()
                        binding.txpSearchMRS2.setText("")
                    }
                    else{
                        listIngredientRecipe.add(it)
                        binding.rcvIngredients.adapter!!.notifyDataSetChanged()
                        binding.txpSearchMRS2.setText("")
                    }
                }
            }
        }

        binding.txvPlusAddIngredientMR2.setOnClickListener {
            binding.txpSearchMRS2.requestFocus()
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.rcvIngredients, InputMethodManager.SHOW_IMPLICIT);
        }
        binding.txvPlusAddStepMR2.setOnClickListener {
            quantSteps++
            listStepRecipe.add(Step(quantSteps,"",false))
            binding.rcvStepsMR2.adapter!!.notifyDataSetChanged()
        }
    }

    private fun initRCVSteps(listSteps:MutableList<Step>) {
        binding.rcvStepsMR2.layoutManager = LinearLayoutManager(this)
        binding.rcvStepsMR2.adapter = StepAdapter(listSteps)
    }

    internal fun initRCVIngredients(listIngredientRecipe:MutableList<Ingredient>) {
        binding.rcvIngredients.layoutManager = LinearLayoutManager(this)
        binding.rcvIngredients.adapter = IngredientAdapter(listIngredientRecipe)
    }
}