package com.settlet.mangia

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Adapter.IngredientAdapter
import com.settlet.mangia.Adapter.IngredientRecipeAdapter
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.databinding.ActivityRecipeBinding

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private val db = Firebase.firestore
    private val storageReference = FirebaseStorage.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeID = intent.getStringExtra("recipeID")

        db.collection("recipes").document(recipeID!!).addSnapshotListener { value, error ->
            if(error!=null){
                Log.w("TAG", "Listen failed.", error)
                return@addSnapshotListener
            }else{
                if (value != null && value.exists()) {
                    val recipe = value.toObject<Recipe>()
                    if (recipe!=null){
                        isSaved(recipe)
                        LoadUserInfo(recipe.publisher)
                        loadRecipeInfo(recipe)
                        loadActualRate(recipe.stars)
                        loadIngredients(recipe.listIngredients)
                        loadViewPager()

                        binding.txvValorationAR.setOnClickListener {
                            val intent = Intent(baseContext, UserRateActivity::class.java )
                            intent.putExtra("recipeID",recipe.recipeID)
                            intent.putExtra("publisherID",recipe.publisher)
                            startActivity(intent)
                        }
                        binding.btnSaveAR.setOnClickListener {
                            val docSaved = hashMapOf<String, Any>()
                            if(binding.btnSaveAR.tag.equals("save")){
                                docSaved["isSaved"] = true.toString()
                                db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).set(docSaved)
                            }else{
                                db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).delete()
                            }
                        }
                    }else{
                        backWithError()
                    }
                }else{
                    backWithError()
                }
            }
        }
    }

    private fun loadRecipeInfo(recipe: Recipe) {
        loadRecipeImage(recipe.listImages.first())
        binding.txvTitleAR.text = recipe.title
        if (recipe.numberTimesValored == 0) {
            binding.txvValorationAR.visibility = View.GONE
        } else {
            binding.txvValorationAR.visibility = View.VISIBLE
        }
        binding.txvValorationAR.text = if (recipe.numberTimesValored == 1) "1 valoración" else "${recipe.numberTimesValored} valoraciones"
    }

    private fun backWithError(){
        onBackPressed()
        Toast.makeText(baseContext,"La receta no se cargó correctamente, por favor, vuelva a intentarlo más tarde.", Toast.LENGTH_LONG).show()
    }

    private fun loadViewPager() {
    }

    private fun loadIngredients(ingredientList: List<Ingredient>) {
        Log.d("ingr",ingredientList.toString())
        binding.rcvIngredientsAR.layoutManager = LinearLayoutManager(this)
        val adapter = IngredientRecipeAdapter()
        binding.rcvIngredientsAR.setHasFixedSize(true)
        adapter.submitList(ingredientList)
        binding.rcvIngredientsAR.adapter = adapter
    }

    private fun loadActualRate(stars: Float) {

    }

    private fun loadRecipeImage(image:String) {
        val fileRef = storageReference.child(image)
        fileRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(this)
                .load(result)
                .into(binding.imvFirstRecipeImageAR)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun LoadUserInfo(email: String) {
        db.collection("users").document(email).get().addOnSuccessListener { document ->
            binding.txvCountryAR.text = document["country"].toString()
            binding.txvUserNameAR.text = "@${document["userName"].toString()}"
            val pImageRef = storageReference.child("users/$email/profile.jpg")
            pImageRef.downloadUrl.addOnSuccessListener { result ->
                Glide.with(this)
                    .load(result)
                    .into(binding.imvProfileAR)
            }
        }.addOnFailureListener {
            backWithError()
        }
    }
    private fun isSaved(recipe: Recipe){
        db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).addSnapshotListener { value, error ->
            if (error!=null){
                Log.w("TAG", "Listen Failed")
                return@addSnapshotListener
            }
            else{
                if (value!=null){
                    if (value.exists()){
                        // cambiar icono a ic_unsave_recipe
                        binding.btnSaveAR.tag = "saved"
                    }else{
                        // cambiar icono a ic_save_recipe
                        binding.btnSaveAR.tag = "save"
                    }
                }
            }
        }
    }
}