package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityEditProfileBinding
import java.util.*
import kotlin.collections.HashMap

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imbBackEP.setOnClickListener {
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.imbSaveEP.setOnClickListener{
            val user = Firebase.auth.currentUser
            if (user!=null) {
                db.collection("users").whereEqualTo("email", user.email.toString()).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val userFB = User(0,document.getString("biography").toString(),
                                0,0,0,0,document.getString("country").toString(),
                                document.getString("dateBirth").toString(),"",document.getString("email").toString(),
                                document.getString("nickName").toString(),"","",document.get("region").toString(),
                                document.getString("userName").toString())
                            updateProfile(binding.txpUNameEP.text.toString(), userFB)
                        }
                    }
            }
        }
        binding.txpDBirthEP.setOnClickListener { showDatePickerDialog() }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
            if (currentUser!=null)
            {
                db.collection("users").whereEqualTo("email",currentUser.email.toString()).get().addOnSuccessListener{ documents ->
                    for (document in documents)
                    {
                        val uName = document.getString("userName")
                        val nName = document.getString("nickName")
                        val bio = document.getString("biography")
                        val country = document.getString("country")
                        val region = document.getString("region")
                        val dBirth = document.getString("dateBirth")
                        binding.txpUNameEP.setText(uName)
                        binding.txpNNameEP.setText(nName)
                        binding.txpBioEP.setText(bio)
                        binding.txpDBirthEP.setText(dBirth)
                        binding.txpCountryEP.setText(country)
                        binding.txpRegionEP.setText(region)
                        /*Glide
                            .with(this)
                            .load(currentUser.photoUrl)
                            .centerCrop()
                            .placeholder(R.drawable.profile_photo)
                            .into(binding.imvProfile)*/
                        Log.d("TAG", "${document.id} => ${document.data}")
                    }
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
                        }else if (binding.txpNNameEP.text.toString()!=userFB.nickName)
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
                        }else if(binding.txpBioEP.text.toString()!=userFB.biography)
                        {
                            userRef.update("biography",binding.txpBioEP.text.toString()).addOnSuccessListener {
                                Log.w("TAG", "Cambio realizado correctamente. ${binding.txpBioEP.text}", )
                                Toast.makeText(baseContext,"El cambio de Biografia a: ${binding.txpBioEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                            }
                        }else if(binding.txpDBirthEP.text.toString()!=userFB.dateBirth)
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
                        }else if(binding.txpCountryEP.text.toString()!=userFB.country)
                        {
                            userRef.update("country",binding.txpCountryEP.text.toString()).addOnSuccessListener {
                                Log.w("TAG", "Cambio realizado correctamente. ${binding.txpCountryEP.text}", )
                                Toast.makeText(baseContext,"El cambio de pais a: ${binding.txpCountryEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                            }
                        }else if(binding.txpRegionEP.text.toString()!=userFB.region)
                        {
                            userRef.update("region",binding.txpRegionEP.text.toString()).addOnSuccessListener {
                                Log.w("TAG", "Cambio realizado correctamente. ${binding.txpRegionEP.text}", )
                                Toast.makeText(baseContext,"El cambio de region a: ${binding.txpRegionEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }