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
                        val userRef = db.collection("users").document(user.email.toString())
                        if(binding.txpUNameEP.text.toString()!=userFB.userName)
                        {
                            userRef.update("userName",binding.txpUNameEP.text.toString()).addOnSuccessListener {
                                Log.w("TAG", "Cambio realizado correctamente. ${binding.txpUNameEP.text}", )
                                Toast.makeText(baseContext,"El cambio de Nombre de usuario a: ${binding.txpUNameEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                            }
                        }else if (binding.txpNNameEP.text.toString()!=userFB.nickName)
                        {
                            userRef.update("nickName",binding.txpNNameEP.text.toString()).addOnSuccessListener {
                                Log.w("TAG", "Cambio realizado correctamente. ${binding.txpNNameEP.text}", )
                                Toast.makeText(baseContext,"El cambio de Apodo a: ${binding.txpNNameEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                            }
                        }else if(binding.txpBioEP.text.toString()!=userFB.biography)
                        {
                            userRef.update("biography",binding.txpBioEP.text.toString()).addOnSuccessListener {
                                Log.w("TAG", "Cambio realizado correctamente. ${binding.txpBioEP.text}", )
                                Toast.makeText(baseContext,"El cambio de Biografia a: ${binding.txpBioEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
                            }
                        }else if(binding.txpDBirthEP.text.toString()!=userFB.dateBirth)
                        {
                            userRef.update("dateBirth",binding.txpDBirthEP.text.toString()).addOnSuccessListener {
                                Log.w("TAG", "Cambio realizado correctamente. ${binding.txpDBirthEP.text}", )
                                Toast.makeText(baseContext,"El cambio de fecha de nacimiento a: ${binding.txpDBirthEP.text} se ha realizado correctamente", Toast.LENGTH_LONG).show()
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