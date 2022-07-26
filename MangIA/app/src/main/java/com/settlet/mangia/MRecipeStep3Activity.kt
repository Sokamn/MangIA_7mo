package com.settlet.mangia

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
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
import kotlinx.android.synthetic.main.activity_mrecipe_step3.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    private lateinit var selectedUnity:String
    private lateinit var  uniqueImage:String
    private var progressComplexity = 0
    private var listimagePath: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Firebase.firestore
        binding = ActivityMrecipeStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val timeFormat =  resources.getStringArray(R.array.timeFormat)
        val arrayAdapterTimeFormat = ArrayAdapter<String>(this, R.layout.spinner_unity_item, timeFormat)
        binding.spnTimeFormat.adapter = arrayAdapterTimeFormat
        binding.spnTimeFormat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0 != null) {
                    selectedUnity = p0.getItemAtPosition(p2).toString()
                    when(selectedUnity){
                        "Minutos"-> binding.sldrPreparationTime.max = 60
                        "Horas"->binding.sldrPreparationTime.max = 24
                        "Días"->binding.sldrPreparationTime.max = 30
                        "Meses"->binding.sldrPreparationTime.max = 12
                        else -> Toast.makeText(baseContext, "Error al seleccionar un formato",Toast.LENGTH_SHORT).show()
                    }
                }
                binding.sldrPreparationTime.progress = 0
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        binding.sldrPreparationTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p1 == 1){
                    when(binding.spnTimeFormat.selectedItem){
                        "Minutos"-> binding.txpPreparationTime.setText("$p1 Minuto")
                        "Horas"-> binding.txpPreparationTime.setText("$p1 Hora")
                        "Días"-> binding.txpPreparationTime.setText("$p1 Día")
                        "Meses"-> binding.txpPreparationTime.setText("$p1 Mes")
                        else -> Toast.makeText(baseContext, "Error al seleccionar un formato",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    binding.txpPreparationTime.setText("$p1 ${binding.spnTimeFormat.selectedItem}")
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        binding.imvComplex0.setOnClickListener {
            ApplyNewComplexity(0)
        }
        binding.imvComplex1.setOnClickListener {
            ApplyNewComplexity(1)
        }
        binding.imvComplex2.setOnClickListener {
            ApplyNewComplexity(2)
        }
        binding.imvComplex3.setOnClickListener {
            ApplyNewComplexity(3)
        }
        binding.imvComplex4.setOnClickListener {
            ApplyNewComplexity(4)
        }
        binding.imvBackMR3.setOnClickListener {
            onBackPressed()
        }
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
            val ing = Ingredient(intent.getStringExtra("ingr$i")!!,intent.getStringExtra("unity$i")!!,0F,intent.getIntExtra("cantIngr$i",0),null)
            ing.unidad = intent.getStringExtra("unity$i")!!
            ing.cant = intent.getIntExtra("cantIngr$i",0)
            listIngredient.add(ing)
        }
        for(i in 1..quantStep){
            val s = Step(i,true)
            s.sDescription = intent.getStringExtra("step$i")!!
            if(intent.getStringExtra("mayImage$i")!! == "null"){
                s.optionalImage = null
            }else{
                s.optionalImage = intent.getStringExtra("mayImage$i")!!
            }
            listStep.add(s)
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
            var i = 0
            var j = 0
            if (user!=null) {
            val userRef = db.collection("users").document(user.email.toString())
                userRef.get()
                    .addOnSuccessListener { document ->
                            val userFB = User(0,document.getString("biography").toString(),
                                0,0,document.getLong("cantRecipes")!!.toInt(),0,document.getString("country").toString(),
                                document.getString("dateBirth").toString(),"",document.getString("email").toString(),
                                document.getString("nickName").toString(),"","",document.get("region").toString(),
                                document.getString("userName").toString())
                                userFB.cantRecipes+=1
                                userRef.update("cantRecipes",userFB.cantRecipes)
                            val docRecipeMI = hashMapOf<String, Any>()
                            val docID = db.collection("recipes").document().id
                            listStep.forEach { step ->
                            if(step.optionalImage!=null){
                                j++
                                val fileRef = storageReference.child("recipes/" + FirebaseAuth.getInstance().currentUser!!.uid + "/recipe${docID}OptionalImage/optionalImage$j")
                                fileRef.putFile(step.optionalImage!!.toUri()).addOnSuccessListener {
                                    Log.d("imageUpload", "Imagen subida correctamente")
                                }.addOnFailureListener{
                                    Log.d("imageUpload", "Imagen no se ha subido correctamente")
                                }
                            }
                        }
                        if (isMultiImages){
                                    listimagePath.clear()
                                    listImages.forEach { img ->
                                        i++
                                        val fileRef = storageReference.child("recipes/" + FirebaseAuth.getInstance().currentUser!!.uid + "/recipe${docID}Image$i.jpg")
                                        fileRef.putFile(img.toUri()).addOnSuccessListener {
                                            Log.d("imageUpload", "Imagen subida correctamente")
                                        }
                                            .addOnFailureListener{
                                                Log.d("imageUpload", "Imagen no se ha subido correctamente")
                                            }
                                        listimagePath.add(fileRef.path)
                                        docRecipeMI["listImages"] = listimagePath
                                    }
                                    docRecipeMI["recipeID"] = docID
                                    docRecipeMI["numberTimesValored"] = 0
                                    docRecipeMI["stars"] = 0
                                    docRecipeMI["timeLaunch"] = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString()
                                    docRecipeMI["cantComments"] = 0
                                    docRecipeMI["title"] = binding.txpTitle.text.toString()
                                    docRecipeMI["description"] = binding.txpDescription.text.toString()
                                    docRecipeMI["publisher"] = userFB.email
                                    docRecipeMI["listIngredients"] = listIngredient
                                    docRecipeMI["listSteps"] = listStep
                                    docRecipeMI["isVegetarian"] = binding.chbVegetarian.isChecked.toString()
                                    docRecipeMI["isVegan"] = binding.chbVegan.isChecked.toString()
                                    docRecipeMI["isDiabetic"] = binding.chbDiabetic.isChecked.toString()
                                    docRecipeMI["isCeliac"] = binding.chbCeliac.isChecked.toString()
                                    docRecipeMI["complexity"] = progressComplexity
                                    docRecipeMI["preparationTime"] = binding.txpPreparationTime.text.toString()

                                    db.collection("recipes").document(docID).set(docRecipeMI)
                                }else{
                                    val fileRef = storageReference.child("recipes/" + FirebaseAuth.getInstance().currentUser!!.uid + "/recipe${docID}Image.jpg")
                                    fileRef.putFile(uniqueImage.toUri()).addOnSuccessListener {
                                        Log.d("imageUpload", "Imagen subida correctamente")
                                    }
                                        .addOnFailureListener{
                                            Log.d("imageUpload", "Imagen no se ha subido correctamente")
                                        }
                                    listimagePath.clear()
                                    listimagePath.add(fileRef.path)
                                    val docRecipeUI = hashMapOf(
                                        "recipeID" to docID,
                                        "stars" to 0,
                                        "numberTimesValored" to 0,
                                        "timeLaunch" to LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString(),
                                        "title" to binding.txpTitle.text.toString(),
                                        "listImages" to listimagePath,
                                        "description" to binding.txpDescription.text.toString(),
                                        "publisher" to userFB.email,
                                        "listIngredients" to listIngredient,
                                        "listSteps" to listStep,
                                        "isVegetarian" to binding.chbVegetarian.isChecked.toString(),
                                        "isVegan" to binding.chbVegan.isChecked.toString(),
                                        "isDiabetic" to binding.chbDiabetic.isChecked.toString(),
                                        "isCeliac" to binding.chbCeliac.isChecked.toString(),
                                        "complexity" to progressComplexity,
                                        "cantComments" to 0,
                                        "preparationTime" to binding.txpPreparationTime.text.toString()
                                    )
                                    db.collection("recipes").document(docID).set(docRecipeUI)
                                }
                    }
            }
            this.finish()
            startActivity(Intent(this,HomeActivity::class.java))
        }
        //https://www.istockphoto.com/es/foto/de-pasta-italiana-verter-sobre-fondo-blanco-gm467084686-60661934
    }

    private fun ApplyNewComplexity(i: Int) {
        if (progressComplexity!=i){
            progressComplexity = i
            when(i){
                0->{
                    binding.imvComplex1.setImageResource(R.drawable.ic_remove)
                    binding.imvComplex2.setImageResource(R.drawable.ic_remove)
                    binding.imvComplex3.setImageResource(R.drawable.ic_remove)
                    binding.imvComplex4.setImageResource(R.drawable.ic_remove)
                    Toast.makeText(this,"Complejidad: Principiante.",Toast.LENGTH_SHORT).show()
                }
                1->{
                    binding.imvComplex1.setImageResource(R.drawable.ic_add)
                    binding.imvComplex2.setImageResource(R.drawable.ic_remove)
                    binding.imvComplex3.setImageResource(R.drawable.ic_remove)
                    binding.imvComplex4.setImageResource(R.drawable.ic_remove)
                    Toast.makeText(this,"Complejidad: Medio.",Toast.LENGTH_SHORT).show()
                }
                2->{
                    binding.imvComplex1.setImageResource(R.drawable.ic_add)
                    binding.imvComplex2.setImageResource(R.drawable.ic_add)
                    binding.imvComplex3.setImageResource(R.drawable.ic_remove)
                    binding.imvComplex4.setImageResource(R.drawable.ic_remove)
                    Toast.makeText(this,"Complejidad: Dificil.",Toast.LENGTH_SHORT).show()
                }
                3->{
                    binding.imvComplex1.setImageResource(R.drawable.ic_add)
                    binding.imvComplex2.setImageResource(R.drawable.ic_add)
                    binding.imvComplex3.setImageResource(R.drawable.ic_add)
                    binding.imvComplex4.setImageResource(R.drawable.ic_remove)
                    Toast.makeText(this,"Complejidad: Ultra Dificil.",Toast.LENGTH_SHORT).show()
                }
                4->{
                    binding.imvComplex1.setImageResource(R.drawable.ic_add)
                    binding.imvComplex2.setImageResource(R.drawable.ic_add)
                    binding.imvComplex3.setImageResource(R.drawable.ic_add)
                    binding.imvComplex4.setImageResource(R.drawable.ic_add)
                    Toast.makeText(this,"Complejidad: Imposible.",Toast.LENGTH_SHORT).show()
                }
                else->{
                    binding.imvComplex1.setImageResource(R.drawable.ic_remove)
                    binding.imvComplex2.setImageResource(R.drawable.ic_remove)
                    binding.imvComplex3.setImageResource(R.drawable.ic_remove)
                    binding.imvComplex4.setImageResource(R.drawable.ic_remove)
                    Toast.makeText(this,"Complejidad: Principiante.",Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}