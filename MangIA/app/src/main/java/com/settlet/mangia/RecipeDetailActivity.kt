package com.settlet.mangia

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.PreviewRecipeAdapter
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.databinding.ActivityRecipeDetailBinding
import kotlinx.android.synthetic.main.bottom_bar.view.*

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    private val recipeList: MutableList<Recipe> = mutableListOf()
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val prefs = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val recipeID = prefs.getString("recipeID","none")
        ReadRecipe(recipeID.toString())
        val adapter = PreviewRecipeAdapter()
        adapter.submitList(recipeList)
        binding.rcvUniqueRecipe.adapter = adapter

        binding.imbBackARD.setOnClickListener {
            onBackPressed()
        }

        binding.txvTitleARD
    }
    private fun ReadRecipe(recipeID: String){
        db.collection("recipes").document(recipeID).addSnapshotListener { value, error ->
            recipeList.clear()
            if(error!=null){
                Log.w("TAG","Listen Failed")
                return@addSnapshotListener
            }
            if(value!=null) {
                val adapter = PreviewRecipeAdapter()
                binding.rcvUniqueRecipe.setHasFixedSize(true)
                binding.rcvUniqueRecipe.layoutManager = LinearLayoutManager(this)
                recipeList.add(value.toObject()!!)
                adapter.submitList(recipeList)
                binding.rcvUniqueRecipe.adapter = adapter
            }
        }
    }
}