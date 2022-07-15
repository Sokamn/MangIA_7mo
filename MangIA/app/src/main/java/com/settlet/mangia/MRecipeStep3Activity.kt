package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.Model.Step
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.ActivityMrecipeStep3Binding
import java.util.*

class MRecipeStep3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMrecipeStep3Binding
    private var isMultiImages:Boolean = false
    private  var listImages = mutableListOf<String>()
    private var listIngredient = mutableListOf<Ingredient>()
    private var listStep = mutableListOf<Step>()
    private var quantIngred = 0
    private val storageReference = FirebaseStorage.getInstance().reference
    private var quantStep = 0
    private lateinit var  uniqueImage:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Firebase.firestore
        binding = ActivityMrecipeStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        isMultiImages = intent.getBooleanExtra("isMultiImages",false)
        if (isMultiImages){
            for (i in 1..intent.getIntExtra("cant",0)){
                listImages.add(intent.getStringExtra("image$i")!!)
            }
            Glide.with(this)
                .load(listImages.first().toUri())
                .fitCenter()
                .into(binding.imvRecipePhotoMR3)
        }else{
            uniqueImage = intent.getStringExtra("uniqueImage")!!
            Glide.with(this)
                .load(uniqueImage.toUri())
                .fitCenter()
                .into(binding.imvRecipePhotoMR3)
        }
        quantIngred = intent.getIntExtra("cantIngredients",0)
        quantStep = intent.getIntExtra("cantSteps",0)
        for(i in 1..quantIngred){
            listIngredient.add(Ingredient(intent.getStringExtra("ingr$i")!!,intent.getStringExtra("unity$i")!!,0F,intent.getIntExtra("ingr$i",0),null))
        }
        for(i in 1..quantStep){
            listStep.add(Step(i,intent.getStringExtra("step$i")!!))
        }

        binding.chbVegan.setOnClickListener {
            if(binding.chbVegan.isChecked){
                binding.chbVegetarian.isChecked = true
                binding.chbVegetarian.isEnabled = false
            }else{
                binding.chbVegetarian.isChecked = false
                binding.chbVegetarian.isEnabled = true
            }
        }
        binding.chbVegetarian.setOnClickListener {
            if(binding.chbVegetarian.isChecked){
                binding.chbVegan.isChecked = false
                binding.chbVegan.isEnabled = false
            }else{
                binding.chbVegan.isEnabled = true
            }
        }

        binding.imvFinishRecipe.setOnClickListener{
            val user = Firebase.auth.currentUser
            if (user!=null) {
            val userRef = db.collection("users").document(user.email.toString())
                db.collection("users").whereEqualTo("email", user.email.toString()).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val userFB = User(0,document.getString("biography").toString(),
                                0,0,0,
                                document.getLong("cantRecipes")!!.toInt(),document.getString("country").toString(),
                                document.getString("dateBirth").toString(),"",document.getString("email").toString(),
                                document.getString("nickName").toString(),"","",document.get("region").toString(),
                                document.getString("userName").toString())
                            userFB.cantRecipes+=1
                            userRef.update("cantRecipes",userFB.cantRecipes)
                                if (isMultiImages){

                                }else{
                                    val fileRef = storageReference.child("recipes/" + FirebaseAuth.getInstance().currentUser!!.uid + "/recipe${userFB.cantRecipes}Image.jpg")
                                    fileRef.putFile(uniqueImage.toUri()).addOnSuccessListener {
                                        Log.d("imageUpload", "Imagen subida correctamente")
                                    }
                                        .addOnFailureListener{
                                            Log.d("imageUpload", "Imagen no se ha subido correctamente")
                                        }
                                    val docRecipe = hashMapOf(
                                        "stars" to 0,
                                        "title" to binding.txpTitle.text.toString(),
                                        "recipeImage" to fileRef.toString(),
                                        "description" to binding.txpDescription.text.toString(),
                                        "publisher" to userFB.userName,
                                        "listIngredients" to listIngredient,
                                        "listSteps" to listStep,
                                        "isVegetarian" to binding.chbVegetarian.isChecked.toString(),
                                        "isVegan" to binding.chbVegan.isChecked.toString(),
                                        "isDiabetic" to binding.chbDiabetic.isChecked.toString(),
                                        "isCeliac" to binding.chbCeliac.isChecked.toString()
                                    )
                                    db.collection("recipes").document(userFB.email).collection("recipe${userFB.cantRecipes}").document("recipe").set(docRecipe)
                                }

                        }
                    }
            }
            finish()
            startActivity(Intent(this,HomeActivity::class.java))
        }
        //https://www.istockphoto.com/es/foto/de-pasta-italiana-verter-sobre-fondo-blanco-gm467084686-60661934
    }
}