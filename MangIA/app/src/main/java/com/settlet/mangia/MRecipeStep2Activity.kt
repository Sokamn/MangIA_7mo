package com.settlet.mangia

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.settlet.mangia.Adapter.IngredientAdapter
import com.settlet.mangia.Adapter.StepAdapter
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.Model.Step
import com.settlet.mangia.Provider.IngredientProvider
import com.settlet.mangia.databinding.ActivityMrecipeStep2Binding
import java.text.Normalizer

class MRecipeStep2Activity : AppCompatActivity() {
    internal val listIngredientRecipe = mutableListOf<Ingredient>()
    internal val listStepRecipe = mutableListOf<Step>()
    var quantSteps: Int = 0
    private lateinit var binding: ActivityMrecipeStep2Binding
    private var isMultiImages:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(com.settlet.mangia.R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
        binding.cstAddIngredient.setOnClickListener {
            binding.txpSearchMRS2.requestFocus()
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
        binding.cstAddStep.setOnClickListener {
            quantSteps++
            listStepRecipe.add(Step(quantSteps,false))
            binding.rcvStepsMR2.adapter!!.notifyDataSetChanged()
        }
        binding.imvBackMRS2.setOnClickListener {
            onBackPressed()
        }
        binding.imvNextStepMR2.setOnClickListener {
            val intent = Intent(this,MRecipeStep3Activity::class.java)
            var countIngr = 0
            var countSteps = 0
            var nullCountS = 0
            var nullCountI = 0
            var stepsNotApplied = mutableListOf<Int>()
            var emptyIngredients = mutableListOf<String>()
            if(listIngredientRecipe.isNotEmpty()){
                if (listStepRecipe.isNotEmpty()){
                    listStepRecipe.forEach {
                        if(it.sDescription==""){
                            nullCountS++
                            stepsNotApplied.add(it.nStep)
                        }
                    }
                    if (nullCountS>0){
                        Toast.makeText(this,"No se han realizado cambios en los siguientes pasos: $stepsNotApplied",Toast.LENGTH_LONG).show()
                    }else{
                        listIngredientRecipe.forEach {
                            if(it.cant == 0){
                                nullCountI++
                                emptyIngredients.add(it.nombre)
                            }
                        }
                        if (nullCountI>0){
                            Toast.makeText(this,"No hay cantidad asignada en los siguientes ingredientes: $emptyIngredients",Toast.LENGTH_LONG).show()
                        }else{
                            for (i in listIngredientRecipe){
                                countIngr++
                                intent.putExtra("ingr$countIngr",i.nombre)
                                intent.putExtra("cantIngr$countIngr", i.cant)
                                intent.putExtra("unity$countIngr", i.unidad)
                            }
                            intent.putExtra("cantIngredients", listIngredientRecipe.size)
                            for (s in listStepRecipe){
                                countSteps++
                                intent.putExtra("step$countSteps",s.sDescription)
                                intent.putExtra("mayImage$countSteps",s.optionalImage.toString())
                            }
                            intent.putExtra("cantSteps", listStepRecipe.size)
                            isMultiImages = getIntent().getBooleanExtra("isMultiImages",false)
                            Log.d("MULTIMAGE", isMultiImages.toString())
                            if(isMultiImages){
                                var cantImages = getIntent().getIntExtra("cant",0)
                                for (i in 1..cantImages){

                                    intent.putExtra("image$i",getIntent().getStringExtra("image$i"))
                                }
                                intent.putExtra("cant",cantImages)
                            }else{
                                intent.putExtra("uniqueImage",getIntent().getStringExtra("uniqueImage")!!)
                            }
                            intent.putExtra("isMultiImages",isMultiImages)
                            startActivity(intent)
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Agregue pasos a la receta.",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Agregue ingredientes a la receta.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRCVSteps(listSteps:MutableList<Step>) {
        binding.rcvStepsMR2.layoutManager = LinearLayoutManager(this)
        val adapter = StepAdapter(this)
        binding.rcvStepsMR2.adapter = adapter
        adapter.submitList(listSteps)

    }

    private fun initRCVIngredients(listIngredientRecipe:MutableList<Ingredient>) {
        binding.rcvIngredients.layoutManager = LinearLayoutManager(this)
        val adapter = IngredientAdapter()
        binding.rcvIngredients.adapter = adapter
        adapter.submitList(listIngredientRecipe)
    }
}