package com.settlet.mangia

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.blongho.country_data.World
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.DatePickerFragment
import com.settlet.mangia.Model.User
import com.settlet.mangia.Provider.countryProvider
import com.settlet.mangia.databinding.ActivityEditProfileBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val storageReference = FirebaseStorage.getInstance().reference
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        val inputUri = uri
        val outputUri = File(filesDir,"croppedImage.jpg").toUri()
        val listUri = listOf<Uri>(inputUri,outputUri)
        cropImage.launch(listUri)
    }
    private val uCropContract = object: ActivityResultContract<List<Uri>, Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f,5f)
                .withMaxResultSize(512,512)
            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }
    }
    private val cropImage = registerForActivityResult(uCropContract){ uri ->
        binding.imvProfileEP.setImageURI(uri)
        uploadImageToFirebase(uri)
    }

    private fun uploadImageToFirebase(image: Uri) {
        var fileRef = storageReference.child("users/" + Firebase.auth.currentUser!!.email + "/profile.jpg")
        fileRef.putFile(image).addOnSuccessListener {
            onBackPressed()
            finish()
            Log.d("imageUpload", "Imagen subida correctamente")
        }
            .addOnFailureListener{
                Log.d("imageUpload", "Imagen no se ha subido correctamente")
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        World.init(applicationContext)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val continents = resources.getStringArray(R.array.continents)
        val arrayAdapterC = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, continents)
        val arrayAdapterLAfrica = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lAfrican)
        val arrayAdapterLAsia = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lAsia)
        val arrayAdapterLASouth = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lSAmerica)
        val arrayAdapterLANorth = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lNAmerica)
        val arrayAdapterLOceania = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lOceania)
        val arrayAdapterLEurope = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lEurope)

        binding.imbBackEP.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.txpRegionEP.setAdapter(arrayAdapterC)

        binding.txpRegionEP.setOnClickListener {
            if(binding.txpCountryEP.text.toString() == "")
            {
                binding.txpRegionEP.isFocusableInTouchMode = true
                binding.txpCountryEP.isFocusableInTouchMode = false
                binding.txpRegionEP.requestFocus()
            }
            else
            {
                binding.txpCountryEP.setText("")
                binding.txpRegionEP.isFocusableInTouchMode = true
                binding.txpCountryEP.isFocusableInTouchMode = false
                binding.txpRegionEP.requestFocus()
            }
        }
        binding.txpCountryEP.setOnClickListener {
            when(binding.txpRegionEP.text.toString()){
                "Africa"->{
                    Log.w("TAG", "${countryProvider.lAfrican}")
                    binding.txpCountryEP.setAdapter(arrayAdapterLAfrica)
                    binding.txpCountryEP.isFocusableInTouchMode = true
                    binding.txpCountryEP.requestFocus()
                    binding.txpRegionEP.isFocusableInTouchMode = false
                }
                "Asia"->{
                    Log.w("TAG", "${countryProvider.lAsia}")
                    binding.txpCountryEP.setAdapter(arrayAdapterLAsia)
                    binding.txpCountryEP.isFocusableInTouchMode = true
                    binding.txpCountryEP.requestFocus()
                    binding.txpRegionEP.isFocusableInTouchMode = false
                }
                "America del Norte o Central"->{
                    Log.w("TAG", "${countryProvider.lNAmerica}")
                    binding.txpCountryEP.setAdapter(arrayAdapterLANorth)
                    binding.txpCountryEP.isFocusableInTouchMode = true
                    binding.txpCountryEP.requestFocus()
                    binding.txpRegionEP.isFocusableInTouchMode = false
                }
                "America del Sur"->{
                    Log.w("TAG", "${countryProvider.lSAmerica}")
                    binding.txpCountryEP.setAdapter(arrayAdapterLASouth)
                    binding.txpCountryEP.isFocusableInTouchMode = true
                    binding.txpCountryEP.requestFocus()
                    binding.txpRegionEP.isFocusableInTouchMode = false
                }
                "Europa"->{
                    Log.w("TAG", "${countryProvider.lEurope}")
                    binding.txpCountryEP.setAdapter(arrayAdapterLEurope)
                    binding.txpCountryEP.isFocusableInTouchMode = true
                    binding.txpCountryEP.requestFocus()
                    binding.txpRegionEP.isFocusableInTouchMode = false
                }
                "Oceania"->{
                    Log.w("TAG", "${countryProvider.lOceania}")
                    binding.txpCountryEP.setAdapter(arrayAdapterLOceania)
                    binding.txpCountryEP.isFocusableInTouchMode = true
                    binding.txpCountryEP.requestFocus()
                    binding.txpRegionEP.isFocusableInTouchMode = false
                }
                else ->{
                    Log.w("TAG", "VACIO ABSOLUTO")
                    binding.txpCountryEP.setAdapter(null)
                    Toast.makeText(this,"Primero ingrese su región / Continente",Toast.LENGTH_LONG).show()
                    binding.txpRegionEP.requestFocus()
                }
            }
        }

        binding.imbSaveEP.setOnClickListener{
            val user = Firebase.auth.currentUser
            if (user!=null) {
                db.collection("users").document( user.email.toString()).get()
                    .addOnSuccessListener { document ->
                            val userFB = document.toObject<User>()
                            updateProfile(binding.txpUNameEP.text.toString(), userFB!!)
                    }
            }
        }
        binding.txpDBirthEP.setOnClickListener { showDatePickerDialog() }

        binding.imvEditProfilePicture.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if (currentUser!=null)
            {
                db.collection("users").document(currentUser.email.toString()).get().addOnSuccessListener{ document ->
                    val user = document.toObject<User>()
                    if (user!=null){
                        binding.txpUNameEP.setText(user.userName)
                        binding.txpNNameEP.setText(user.nickName)
                        binding.txpBioEP.setText(user.biography)
                        binding.txpCountryEP.setText(user.country)
                        binding.txpRegionEP.setText(user.region)
                        binding.txpDBirthEP.setText(user.dateBirth)
                        Log.d("TAG", "${document.id} => ${document.data}")
                    }
                }
                val pImageRef = storageReference.child("users/${currentUser.email}/profile.jpg")
                pImageRef.downloadUrl.addOnSuccessListener { result ->
                    Glide.with(this)
                        .load(result)
                        .into(binding.imvProfileEP)
                }
            }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day: Int, month: Int, year: Int){
        binding.txpDBirthEP.setText("$day/${month+1}/$year")
    }


    private fun updateProfile(name: String, userFB: User){
        val user = Firebase.auth.currentUser
        var aux1 = 0
        var aux2 = 0
        if(binding.txpUNameEP.text.toString()==userFB.userName && binding.txpNNameEP.text.toString()==userFB.nickName && binding.txpBioEP.text.toString()==userFB.biography && binding.txpDBirthEP.text.toString()==userFB.dateBirth && binding.txpCountryEP.text.toString()==userFB.country && binding.txpRegionEP.text.toString()==userFB.region)
        {
            Toast.makeText(baseContext,"No se han detectado cambios.", Toast.LENGTH_SHORT).show()
        }
        else{
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful)
                    {
                        val badWords = arrayListOf<String>("sorete","imbecil","tarado","pelotudo","pajero","pajera","pelotuda","tarada","puto","puta","concha","culo","poronga","verga","pito","pene" + "nigga" , "trola" , "trolo" , "caca" , "down" , "mierda" , "nazi" , "hitler" , "estupido" , "coger" , "cojer" , "pendejo " , "pendeja" , "porno" , "orto" , "sexo" , "pinche" , "pinchi" , "cojo" , "cabrón" , "cabrona" , "mames" , "pendejos" , "pendejas" , "chinga" , "mamadas" , "pendejadas" , "mama huevo" , "pete" , "wueon" , "xuxa" , "weon" , "weonado" , "weona" , "coño" , "aguevoniado" , "guevon" , "pajuo" , "marica", "monda" , "marrana" , "marrano" ,"monda" , "pijudo" , "hijueputa" , "cotopla" , "pichurria" , "picha" , "mother fucker" , "fuck" , "ass" , "orgy" , "bitch" , "suck" , "my balls" , "slut " , "whore" , "hoe" , "chupamela" , "culito" , "cojida" , "cojiendo" , "zoofilia" , "putito" , "reputo" , "free viagra" , "taradito", "taradita" , "pelotudito" , "pelotudita" , "pelotuditos", "pelotuditas" , "putita" , "poronguita" , "verguita" , "pitito" , "trolito" , "trolita" , "caquita" , "estupidito" , "estupidita" , "pendejito" , "pendejita" , "putitos" , "putitas" , "poronguitas" , "porongotas" , "porongota" , "porongon" , "verguitas", "vergotas" , "vergota" , "pititos" , "pitotes" , "pitote" , "trolitos" , "trolitas" , "caquitas" , "cacotas" , "estupiditos" , "estupiditas" , "pendejitos" , "pendejitas" , "feto" , "cigoto" , "caka" , "kaka" , "kk" , "joto" , "jota" , "kaco" , "kago" , "kojo" , "kulo" , "mamo" , "meaas" , "mion" , "mula" , "pedo" , "qulo" , "buey" , "caco" , "cago" , "cako" , "coja" , "coji" , "guey" , "kaca" , "kaga" , "koge" , "mame" , "mear" , "meon" , "moco")
                        val userRef = db.collection("users").document(user.email.toString())
                        if(binding.txpUNameEP.text.toString()!=userFB.userName)
                        {
                            for(t in badWords)
                            {
                                if(binding.txpUNameEP.text.toString() == t)
                                {
                                    Toast.makeText(this,"Se detectó un nombre de usuario o apodo ofensivo.",Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    userRef.update("userName",binding.txpUNameEP.text.toString()).addOnSuccessListener {
                                        Log.w("TAG", "Cambio realizado correctamente. ${binding.txpUNameEP.text}", )
                                        Toast.makeText(baseContext,"El cambio de Nombre de usuario a: ${binding.txpUNameEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        if (binding.txpNNameEP.text.toString()!=userFB.nickName)
                        {
                            for(t in badWords) {
                                if (binding.txpNNameEP.text.toString() == t) {
                                    Toast.makeText(
                                        this,
                                        "Se detectó un nombre de usuario o apodo ofensivo.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else{
                                    userRef.update("nickName",binding.txpNNameEP.text.toString()).addOnSuccessListener {
                                        Log.w("TAG", "Cambio realizado correctamente. ${binding.txpNNameEP.text}", )
                                        Toast.makeText(baseContext,"El cambio de Apodo a: ${binding.txpNNameEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        if(binding.txpBioEP.text.toString()!=userFB.biography)
                        {
                            userRef.update("biography",binding.txpBioEP.text.toString()).addOnSuccessListener {
                                Log.w("TAG", "Cambio realizado correctamente. ${binding.txpBioEP.text}", )
                                Toast.makeText(baseContext,"El cambio de Biografia a: ${binding.txpBioEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                            }
                        }
                        if(binding.txpDBirthEP.text.toString()!=userFB.dateBirth)
                        {
                            val edad = Calendar.getInstance().get(Calendar.YEAR)-binding.txpDBirthEP.text.substring(binding.txpDBirthEP.text.length-4).trim().toInt()
                            if(edad<13)
                            {
                                Toast.makeText(this, "Usted tiene menos de 13 años, por favor, ingrese una fecha valida.",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                userRef.update("dateBirth",binding.txpDBirthEP.text.toString()).addOnSuccessListener {
                                    Log.w("TAG", "Cambio realizado correctamente. ${binding.txpDBirthEP.text}", )
                                    Toast.makeText(baseContext,"El cambio de fecha de nacimiento a: ${binding.txpDBirthEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        if(binding.txpCountryEP.text.toString()!=userFB.country)
                        {
                            for (C in countryProvider.lTPaises)
                            {
                                if(binding.txpCountryEP.text.toString() == C)
                                {
                                    aux1++
                                    userRef.update("country",binding.txpCountryEP.text.toString()).addOnSuccessListener {
                                        Log.w("TAG", "Cambio realizado correctamente. ${binding.txpCountryEP.text}", )
                                        Toast.makeText(baseContext,"El cambio de pais a: ${binding.txpCountryEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            if(aux1==0)
                            {
                                Toast.makeText(this,"Pais seleccionado inexistente. Por favor, seleccione los que les recomendamos.\nTenga en cuenta que los paises están en Ingles",Toast.LENGTH_LONG).show()
                            }
                        }
                        if(binding.txpRegionEP.text.toString()!=userFB.region)
                        {
                            for (c in resources.getStringArray(R.array.continents))
                            {
                                if(binding.txpRegionEP.text.toString() == c) {
                                    aux2++
                                    userRef.update("region", binding.txpRegionEP.text.toString())
                                        .addOnSuccessListener {
                                            Log.w(
                                                "TAG",
                                                "Cambio realizado correctamente. ${binding.txpRegionEP.text}",
                                            )
                                            Toast.makeText(
                                                baseContext,
                                                "El cambio de region a: ${binding.txpRegionEP.text} se ha realizado correctamente",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                            }
                            if(aux2==0)
                            {
                                Toast.makeText(this,"Continente seleccionado inexistente. Por favor, seleccione los que les recomendamos",Toast.LENGTH_SHORT).show()
                            }

                            }
                        }
                    }
                }
            }
        }
