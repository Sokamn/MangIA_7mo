package com.settlet.mangia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    private val currentUserID = Firebase.auth.currentUser!!.uid
    private val reference = FirebaseDatabase.getInstance().reference
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
                        isSaved(recipe.recipeID)
                        LoadUserInfo(recipe.publisher)
                        loadRecipeInfo(recipe)
                        loadIngredients(recipe.listIngredients)
                        loadViewPager()

                        binding.txvValorationAR.setOnClickListener {
                            val intent = Intent(baseContext, UserRateActivity::class.java )
                            intent.putExtra("recipeID",recipe.recipeID)
                            intent.putExtra("publisherID",recipe.publisher)
                            startActivity(intent)
                        }
                        binding.btnSaveAR.setOnClickListener {
                            if(binding.btnSaveAR.tag.equals("save")){
                                reference.child("saves").child(currentUserID).child(recipe.recipeID).setValue(true)
                                //db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).set(docSaved)
                            }else{
                                reference.child("saves").child(currentUserID).child(recipe.recipeID).removeValue()
                                //db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).delete()
                            }
                            /*val docSaved = hashMapOf<String, Any>()
                            if(binding.btnSaveAR.tag.equals("save")){
                                docSaved["isSaved"] = true.toString()
                                db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).set(docSaved)
                            }else{
                                db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).delete()
                            }*/
                        }
                        binding.toolbarRecipe.setOnClickListener {
                            val editor = getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                            editor.putString("profileID", recipe.publisher)
                            editor.apply()
                            startActivity(Intent(this, ProfileActivity::class.java))
                        }
                        binding.btnCommentsAR.setOnClickListener {
                            val intent = Intent(this, CommentsActivity::class.java )
                            intent.putExtra("recipeID",recipeID)
                            startActivity(intent)
                        }
                        binding.btnValorateAR.setOnClickListener {

                        }
                        binding.txvValorationAR.setOnClickListener {
                            val intent = Intent(this, UserRateActivity::class.java )
                            intent.putExtra("recipeID",recipe.recipeID)
                            intent.putExtra("publisherID",recipe.publisher)
                            startActivity(intent)
                        }
                    }else{
                        backWithError()
                    }
                }else{
                    backWithError()
                }
            }
        }
        binding.appBarAR.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
            var scrollRange = -1
            override fun onOffsetChanged(
                appBarLayout: AppBarLayout?,
                verticalOffset: Int
            ) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange;
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.imvProfileAR.visibility = View.GONE
                }else{
                    binding.imvProfileAR.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun loadRecipeInfo(recipe: Recipe) {
        loadRecipeImage(recipe.listImages.first())
        binding.txvTitleAR.text = recipe.title
        reference.child("likes").child(recipe.recipeID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                when(snapshot.childrenCount.toString()){
                    "0"-> binding.txvValorationAR.visibility = View.GONE
                    "1"-> binding.txvValorationAR.text = "1 valoración"
                    else -> binding.txvValorationAR.text = "${snapshot.childrenCount} valoraciones"
                }
                loadActualRate(recipe.recipeID, snapshot.childrenCount)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
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

    private fun loadActualRate(recipeID: String, cantValorations: Long) {
        reference.child("recipes").child(recipeID).child("totalValoration").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val averageValoration = snapshot.value.toString().toLong() / cantValorations
                if (averageValoration > 0 && averageValoration < 0.5){ // 0 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar2AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 0.5 && averageValoration < 1){ // 0.5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.mid_star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }
                if (averageValoration >= 1 && averageValoration < 1.5){ // 1 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 1.5 && averageValoration < 2) { // 1.5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.mid_star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }
                if (averageValoration >= 2 && averageValoration < 2.5){ // 2 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 2.5 && averageValoration < 3){ // 2.5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.mid_star_average)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }
                if (averageValoration >= 3 && averageValoration < 3.5){ // 3 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_average)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration > 3.5 && averageValoration < 4) { // 3.5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_average)
                    binding.imvStar4AR.setImageResource(R.drawable.mid_star_average)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }
                if (averageValoration > 4 && averageValoration <= 4.5){ // 4 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_average)
                    binding.imvStar4AR.setImageResource(R.drawable.star_average)
                    binding.imvStar5AR.setImageResource(R.drawable.mid_star_average)
                }else if (averageValoration > 4.5 && averageValoration <= 5){ // 5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_average)
                    binding.imvStar4AR.setImageResource(R.drawable.star_average)
                    binding.imvStar5AR.setImageResource(R.drawable.star_average)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun loadRecipeImage(image:String) {
        val fileRef = storageReference.child(image)
        fileRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(this)
                .load(result)
                .into(binding.imvFirstRecipeImageAR)
        }
    }

    private fun LoadUserInfo(profileID: String) {
        reference.child("users").child(profileID).get().addOnSuccessListener{ user ->
            binding.txvCountryAR.text = user.child("country").value.toString()
            binding.txvUserNameAR.text = user.child("userName").value.toString()
            val pImageRef = storageReference.child("users/$profileID/profile.jpg")
            pImageRef.downloadUrl.addOnSuccessListener { result ->
                Glide.with(this)
                    .load(result)
                    .into(binding.imvProfileAR)
            }
        }
        /*db.collection("users").document(profileID).get().addOnSuccessListener { document ->
            binding.txvCountryAR.text = document["country"].toString()
            binding.txvUserNameAR.text = "@${document["userName"].toString()}"
            val pImageRef = storageReference.child("users/$profileID/profile.jpg")
            pImageRef.downloadUrl.addOnSuccessListener { result ->
                Glide.with(this)
                    .load(result)
                    .into(binding.imvProfileAR)
            }
        }.addOnFailureListener {
            backWithError()
        }*/
    }
    private fun isSaved(recipeID: String){
        reference.child("saves").child(currentUserID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(recipeID).exists()){
                    // cambiar icono a ic_unsave_recipe
                    binding.btnSaveAR.tag = "saved"
                }else{
                    // cambiar icono a ic_save_recipe
                    binding.btnSaveAR.tag = "save"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        /*db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).addSnapshotListener { value, error ->
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
        }*/
    }
}