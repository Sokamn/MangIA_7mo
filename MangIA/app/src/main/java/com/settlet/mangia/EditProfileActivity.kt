package com.settlet.mangia

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
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
    private lateinit var editPopup: Dialog
    private lateinit var titlePopup: TextView
    private lateinit var editTextPopup: EditText
    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button
    private lateinit var spinnerPopup: Spinner

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

        editPopup = Dialog(this)
        editPopup.setContentView(R.layout.popup_edit_profile)
        editPopup.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        titlePopup = editPopup.findViewById(R.id.txvTitlePEP)
        editTextPopup = editPopup.findViewById(R.id.txpEditablePEP)
        btnCancel = editPopup.findViewById(R.id.btnCancelPEP)
        btnSave = editPopup.findViewById(R.id.btnSavePEP)
        spinnerPopup = editPopup.findViewById(R.id.spnEditablePEP)

        btnCancel.setOnClickListener {

        }

        binding.imbBackEP.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.imvEditProfilePicture.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.imvEditBioEP.setOnClickListener {
            loadInfo("bio")
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

    private fun loadInfo(info: String) {
        val user = Firebase.auth.currentUser
        val userRef = reference.child("users").child(user!!.uid)
        editPopup.show()
        when(info){
            "bio"->{
                titlePopup.setText(R.string.bio)
                editTextPopup.setHint(R.string.bio)
                editTextPopup.setText(binding.txvBiographyEP.text.toString())
                btnSave.setOnClickListener {
                    if(binding.txvBiographyEP.text==editTextPopup.text.toString()){
                        Toast.makeText(baseContext,"No se han detectado cambios.", Toast.LENGTH_SHORT).show()
                    }else{
                        userRef.child("biography").setValue(editTextPopup.text.toString())
                        Toast.makeText(baseContext,"Cambios efectados correctamente.", Toast.LENGTH_SHORT).show()
                    }
                }
                spinnerPopup.visibility = View.INVISIBLE
            }
            "userName"->{
                titlePopup.setText(R.string.userName)
                editTextPopup.setText(binding.txvUNameEP.text.toString())

                btnSave.setOnClickListener {
                    if(binding.txvUNameEP.text==editTextPopup.text.toString()){
                        Toast.makeText(baseContext,"No se han detectado cambios.", Toast.LENGTH_SHORT).show()
                    }else{
                        val profileUpdates = userProfileChangeRequest {
                            displayName = editTextPopup.text.toString()
                        }
                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener{ task ->
                                if(task.isSuccessful)
                                {
                                    val badWords = arrayListOf("sorete","imbecil","tarado","pelotudo","pajero","pajera","pelotuda","tarada","puto","puta","concha","culo","poronga","verga","pito","pene" + "nigga" , "trola" , "trolo" , "caca" , "down" , "mierda" , "nazi" , "hitler" , "estupido" , "coger" , "cojer" , "pendejo " , "pendeja" , "porno" , "orto" , "sexo" , "pinche" , "pinchi" , "cojo" , "cabrón" , "cabrona" , "mames" , "pendejos" , "pendejas" , "chinga" , "mamadas" , "pendejadas" , "mama huevo" , "pete" , "wueon" , "xuxa" , "weon" , "weonado" , "weona" , "coño" , "aguevoniado" , "guevon" , "pajuo" , "marica", "monda" , "marrana" , "marrano" ,"monda" , "pijudo" , "hijueputa" , "cotopla" , "pichurria" , "picha" , "mother fucker" , "fuck" , "ass" , "orgy" , "bitch" , "suck" , "my balls" , "slut " , "whore" , "hoe" , "chupamela" , "culito" , "cojida" , "cojiendo" , "zoofilia" , "putito" , "reputo" , "free viagra" , "taradito", "taradita" , "pelotudito" , "pelotudita" , "pelotuditos", "pelotuditas" , "putita" , "poronguita" , "verguita" , "pitito" , "trolito" , "trolita" , "caquita" , "estupidito" , "estupidita" , "pendejito" , "pendejita" , "putitos" , "putitas" , "poronguitas" , "porongotas" , "porongota" , "porongon" , "verguitas", "vergotas" , "vergota" , "pititos" , "pitotes" , "pitote" , "trolitos" , "trolitas" , "caquitas" , "cacotas" , "estupiditos" , "estupiditas" , "pendejitos" , "pendejitas" , "feto" , "cigoto" , "caka" , "kaka" , "kk" , "joto" , "jota" , "kaco" , "kago" , "kojo" , "kulo" , "mamo" , "meaas" , "mion" , "mula" , "pedo" , "qulo" , "buey" , "caco" , "cago" , "cako" , "coja" , "coji" , "guey" , "kaca" , "kaga" , "koge" , "mame" , "mear" , "meon" , "moco")
                                    if(binding.txvUNameEP.text.toString()!=editTextPopup.text.toString())
                                    {
                                        for(t in badWords)
                                        {
                                            if(editTextPopup.text.toString().contains(t))
                                            {
                                                Toast.makeText(this,"Se detectó un nombre de usuario o apodo ofensivo.",Toast.LENGTH_SHORT).show()
                                            }
                                            else{
                                                userRef.child("userName").setValue(editTextPopup.text.toString())
                                                Toast.makeText(baseContext,"Cambios efectados correctamente.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                    }
                }
                spinnerPopup.visibility = View.INVISIBLE
            }
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
