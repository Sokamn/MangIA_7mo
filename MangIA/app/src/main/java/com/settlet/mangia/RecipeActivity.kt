package com.settlet.mangia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
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
import com.settlet.mangia.Adapter.PagerAdapterStep
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.Model.Step
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
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

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
                        loadViewPager(recipe.listSteps)

                        binding.txvValorationAR.setOnClickListener {
                            val intent = Intent(baseContext, UserRateActivity::class.java )
                            intent.putExtra("recipeID",recipe.recipeID)
                            intent.putExtra("publisherID",recipe.publisher)
                            startActivity(intent)
                        }
                        binding.btnSaveAR.setOnClickListener {
                            if(binding.btnSaveAR.tag.equals("save")){
                                reference.child("saves").child(currentUserID).child(recipe.recipeID).setValue(true)
                            }else{
                                reference.child("saves").child(currentUserID).child(recipe.recipeID).removeValue()
                            }
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
                loadActualRate(recipe.recipeID, snapshot.childrenCount.toString().toDouble())
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun backWithError(){
        onBackPressed()
        Toast.makeText(baseContext,"La receta no se cargó correctamente, por favor, vuelva a intentarlo más tarde.", Toast.LENGTH_LONG).show()
    }

    private fun loadViewPager(listStep: List<Step>) {
        val adapterVP = PagerAdapterStep()
        adapterVP.submitList(listStep)
        binding.vwpContentAR.adapter = adapterVP
        binding.vwpContentAR.clipToPadding = false
        binding.vwpContentAR.clipChildren = false
        binding.vwpContentAR.offscreenPageLimit = 2
        binding.vwpContentAR.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        binding.indicatorStep.setViewPager(binding.vwpContentAR)
    }

    private fun loadIngredients(ingredientList: List<Ingredient>) {
        Log.d("ingr",ingredientList.toString())
        binding.rcvIngredientsAR.layoutManager = LinearLayoutManager(this)
        val adapter = IngredientRecipeAdapter()
        binding.rcvIngredientsAR.setHasFixedSize(true)
        adapter.submitList(ingredientList)
        binding.rcvIngredientsAR.adapter = adapter
    }

    private fun loadActualRate(recipeID: String, cantValorations: Double) {
        reference.child("recipes").child(recipeID).child("totalValoration").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val averageValoration = snapshot.value.toString().toDouble() / cantValorations
                Log.d("PAPAPA", "$averageValoration = ${snapshot.value} | $cantValorations")
                if (averageValoration > 0 && averageValoration < 0.5){ // 0 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar2AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 0.5 && averageValoration < 1.0){ // 0.5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.mid_star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 1.0 && averageValoration < 1.5){ // 1 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 1.5 && averageValoration < 2.0) { // 1.5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.mid_star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 2.0 && averageValoration < 2.5){ // 2 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 2.5 && averageValoration < 3.0){ // 2.5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.mid_star_average)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 3.0 && averageValoration < 3.5){ // 3 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_average)
                    binding.imvStar4AR.setImageResource(R.drawable.star_unselected)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 3.5 && averageValoration < 4.0) { // 3.5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_average)
                    binding.imvStar4AR.setImageResource(R.drawable.mid_star_average)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 4.0 && averageValoration < 4.5){ // 4 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_average)
                    binding.imvStar4AR.setImageResource(R.drawable.star_average)
                    binding.imvStar5AR.setImageResource(R.drawable.star_unselected)
                }else if (averageValoration >= 4.5 && averageValoration < 4.75){ // 5 estrellas
                    binding.imvStar1AR.setImageResource(R.drawable.star_average)
                    binding.imvStar2AR.setImageResource(R.drawable.star_average)
                    binding.imvStar3AR.setImageResource(R.drawable.star_average)
                    binding.imvStar4AR.setImageResource(R.drawable.star_average)
                    binding.imvStar5AR.setImageResource(R.drawable.mid_star_average)
                }else if(averageValoration in 4.75..5.0){ // 5 estrellas
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
    }
    private fun isSaved(recipeID: String){
        reference.child("saves").child(currentUserID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(recipeID).exists()){
                    binding.btnSaveAR.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_unsave_recipe_black),null,null,null)
                    binding.btnSaveAR.tag = "saved"
                }else{
                    binding.btnSaveAR.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_save_recipe_black),null,null,null)
                    binding.btnSaveAR.tag = "save"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
}