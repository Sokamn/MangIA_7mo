package com.settlet.mangia.ViewHolder

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Adapter.IngredientRecipeAdapter
import com.settlet.mangia.Adapter.SliderAdapter
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.ProfileActivity
import com.settlet.mangia.R
import com.settlet.mangia.databinding.RowRecipeSearchedBinding
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.popup_list_ingredients.*
import kotlin.math.roundToInt

class RecipeSearchedViewHolder(view: View): RecyclerView.ViewHolder(view)  {
    val binding = RowRecipeSearchedBinding.bind(view)
    private val db = Firebase.firestore
    private val reference = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference
    val profileID = Firebase.auth.currentUser!!.uid
    private lateinit var ingredients: Dialog
    private lateinit var imagePost: Dialog
    private lateinit var rcvIingredients: RecyclerView
    private lateinit var imvUniquePostDialog : ImageView
    private lateinit var sldrPostImage: SliderView
    private val listImages = mutableListOf<String>()



    fun render(recipe: Recipe){
        loadPostImage(recipe.listImages.first())
        loadInfoRecipe(recipe)
        loadUserName(recipe.publisher)

        ingredients = Dialog(itemView.context)
        ingredients.setContentView(R.layout.popup_list_ingredients)
        ingredients.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        imagePost = Dialog(itemView.context)
        imagePost.setContentView(R.layout.popup_image_recipe)
        imagePost.window!!.setGravity(Gravity.CENTER)
        imagePost.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        imvUniquePostDialog = imagePost.findViewById(R.id.imvUniquePostIR)
        sldrPostImage = imagePost.findViewById(R.id.imgsldrCarruselIR)

        loadPostImages(recipe)

        binding.txvUNameRS.setOnClickListener {
            val editor = itemView.context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileID", recipe.publisher)
            editor.apply()
            itemView.context.startActivity(Intent(itemView.context, ProfileActivity::class.java))
        }
        binding.imvIngredients.setOnClickListener {
            loadIngredients(recipe.listIngredients)
            ingredients.show()
        }
        binding.txvCantIngredientsRS.setOnClickListener {
            loadIngredients(recipe.listIngredients)
            ingredients.show()
        }
        binding.imvUniquePost.setOnClickListener {
            imagePost.show()
        }
    }

    private fun loadPostImages(recipe: Recipe) {
        if (recipe.listImages.size == 1){
            val fileRef = storageReference.child(recipe.listImages.first())
            fileRef.downloadUrl.addOnSuccessListener { result ->
                Glide.with(imagePost.context)
                    .load(result)
                    .into(imvUniquePostDialog)
            }
            sldrPostImage.visibility = View.GONE
        }else{
            recipe.listImages.forEach {
                val fileRef = storageReference.child(it)
                fileRef.downloadUrl.addOnSuccessListener { result ->
                    listImages.add(result.toString())
                    if (listImages.size == recipe.listImages.size){
                        Log.d("IMAGE",listImages.toString())
                        sldrPostImage.setSliderAdapter(SliderAdapter(listImages,false))
                    }
                }.addOnFailureListener {
                    Log.d("IMAGE","No se ha podido cargar la imagen")
                }
            }
            imvUniquePostDialog.visibility = View.INVISIBLE
            sldrPostImage.visibility = View.VISIBLE
        }
    }

    private fun loadIngredients(ingredientList: List<Ingredient>) {
        rcvIingredients = ingredients.findViewById(R.id.rcvIngredientsLI)
        rcvIingredients.layoutManager = LinearLayoutManager(itemView.context)
        val adapter = IngredientRecipeAdapter()
        rcvIingredients.setHasFixedSize(true)
        adapter.submitList(ingredientList)
        rcvIingredients.adapter = adapter
    }

    private fun loadUserName(publisher: String) {
        reference.child("users").child(publisher).child("userName").get().addOnSuccessListener {
            binding.txvUNameRS.text = "@${it.value.toString()}"
        }
    }

    private fun loadInfoRecipe(recipe: Recipe) {
        binding.txvDescriptionRS.text = recipe.description
        binding.txvTitleRecipeRS.text = recipe.title
        binding.txvUNameRS.text = recipe.publisher
        binding.txvCantIngredientsRS.text = recipe.listIngredients.size.toString()
        reference.child("likes").child(recipe.recipeID).get().addOnSuccessListener { snapshot ->
            reference.child("recipes").child(recipe.recipeID).child("totalValoration").get().addOnSuccessListener {
                val average = it.value.toString().toDouble() / snapshot.childrenCount
                val roundoff = (average * 10.0).roundToInt() / 10.0
                if(roundoff.toString().endsWith("0")){
                    binding.txvValorationRS.text = roundoff.roundToInt().toString()
                }
                binding.txvValorationRS.text = roundoff.toString()
            }
        }
    }

    private fun loadPostImage(image: String) {
        val fileRef = storageReference.child(image)
        fileRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(itemView.context)
                .load(result)
                .into(binding.imvUniquePost)
        }
    }
}
