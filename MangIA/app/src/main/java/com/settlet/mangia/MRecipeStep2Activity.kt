package com.settlet.mangia

import android.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.settlet.mangia.databinding.ActivityMrecipeStep2Binding
import com.yalantis.ucrop.UCrop
import java.io.File

class MRecipeStep2Activity : AppCompatActivity() {
    internal val listIngredientRecipe = mutableListOf<Ingredient>()
    internal val listStepRecipe = mutableListOf<Step>()
    var quantSteps: Int = 0
    private lateinit var binding: ActivityMrecipeStep2Binding
    internal var finishedListener:Boolean = false
    internal var auxUri:Uri?=null
    internal val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri!=null)
        {
            val inputUri = uri
            val outputUri = File(filesDir,"croppedImage.jpg").toUri()
            val listUri = listOf<Uri>(inputUri,outputUri)
            cropImage.launch(listUri)
        }
        else{
            Toast.makeText(baseContext,"No has seleccionado ninguna imagen.", Toast.LENGTH_SHORT).show()
            finishedListener = false
        }
    }
    internal val uCropContract = object: ActivityResultContract<List<Uri>, Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f,5f)
                .withMaxResultSize(1080,1080)
            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return if(intent!=null) {
                UCrop.getOutput(intent)!!
            } else {
                null
            }
        }
    }
    internal val cropImage = registerForActivityResult(uCropContract){ uri ->
        finishedListener = if (uri!=null) {
            auxUri = uri
            true
        } else{
            Toast.makeText(baseContext,"No has terminado de recortar una imagen.", Toast.LENGTH_SHORT).show()
            false
        }
    }
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

        binding.cstAddIngredient.setOnClickListener {
            binding.txpSearchMRS2.requestFocus()
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.rcvIngredients, InputMethodManager.SHOW_IMPLICIT)
        }
        binding.cstAddStep.setOnClickListener {
            quantSteps++
            listStepRecipe.add(Step(quantSteps,"",false))
            binding.rcvStepsMR2.adapter!!.notifyDataSetChanged()
        }
        binding.imvBackMRS2.setOnClickListener {
            val intent = Intent(this,MRecipeStep1Activity::class.java)
            startActivity(intent)
        }
        binding.imvNextStepMRS2.setOnClickListener {
            /*val intent = Intent(this,MRecipeStep3Activity::class.java)
            startActivity(intent)*/
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