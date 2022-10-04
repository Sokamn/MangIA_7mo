package com.settlet.mangia

import android.app.Activity
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Adapter.SliderAdapter
import com.settlet.mangia.Model.DatePickerFragment
import com.settlet.mangia.Model.User
import com.settlet.mangia.Provider.countryProvider
import com.settlet.mangia.databinding.ActivityEditProfileBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val reference = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri!=null)
        {
            val inputUri = uri
            val outputUri = File(filesDir,"croppedImage.jpg").toUri()
            val listUri = listOf<Uri>(inputUri,outputUri)
            cropImage.launch(listUri)
        }
        else{
            Toast.makeText(baseContext,"No has seleccionado ninguna imagen.",Toast.LENGTH_SHORT).show()
        }
    }
    private val uCropContract = object: ActivityResultContract<List<Uri>,Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]
            Log.d("URI","${input[0]} ! ${input[1]}")

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f,5f)
                .withMaxResultSize(1080,1080)
            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if(intent!=null)
            {
                return UCrop.getOutput(intent)!!
            }
            else
            {
                return null
            }
        }
    }
    private val cropImage = registerForActivityResult(uCropContract){ uri ->
        if (uri!=null)
        {
            binding.imvProfileEP.setImageURI(uri)
            uploadImageToFirebase(uri)
        }
        else{
            Toast.makeText(baseContext,"No has terminado de recortar una imagen.",Toast.LENGTH_SHORT).show()
        }
    }


    private fun uploadImageToFirebase(image: Uri) {
        var fileRef = storageReference.child("users/" + Firebase.auth.currentUser!!.uid + "/profile.jpg")
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
        window.statusBarColor = getColor(R.color.secundaryColor)

        binding.imbBackEP.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.imvEditProfilePicture.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.imvEditBioEP.setOnClickListener {

        }

        binding.imvEditBDateEP.setOnClickListener {
            showDatePickerDialog()
        }

        binding.imvEditCountryEP.setOnClickListener {

        }

        binding.imvEditNNameEP.setOnClickListener {

        }

        binding.imvEditRegionEP.setOnClickListener {

        }

        binding.imvEditUNameEP.setOnClickListener {

        }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if (currentUser!=null)
            {
                reference.child("users").child(currentUser.uid).get().addOnSuccessListener {
                    val user = it.getValue(User::class.java)
                    if (user!=null){
                        binding.txvUNameEP.text = user.userName
                        binding.txvNNameEP.text = user.nickName
                        binding.txvBiographyEP.text = user.biography
                        binding.txvCountryEP.text = user.country
                        binding.txvRegionEP.text = user.region
                        binding.txvBirthDateEP.text = user.dateBirth
                        binding.txvTitleUNameEP.text = "@"+user.userName
                    }
                }
                val pImageRef = storageReference.child("users/${currentUser.uid}/profile.jpg")
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

    private fun onDateSelected(day: Int, month: Int, year: Int){
        binding.txvBirthDateEP.text = "$day/${month+1}/$year"
        //reference.child("users").child()
    }
}
