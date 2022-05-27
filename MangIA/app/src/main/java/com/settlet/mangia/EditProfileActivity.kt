package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
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
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        binding.imbSaveEP.setOnClickListener{
            val user = auth.currentUser
            var userFB: User? = null
            if (user!=null) {
                db.collection("users").whereEqualTo("email", user.email).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            userFB = document.toObject(User::class.java)
                        }
                    }
                updateProfile(binding.txpUNameEP.text.toString(), userFB!!)
            }
        }
    }
     /*public override fun onStart() {
        super.onStart()
        val user = auth.currentUser
         var userFB: User? = null
         if (user!=null)
         {
             db.collection("users").whereEqualTo("email",user.email.toString()).get().addOnSuccessListener { result ->
                 for (document in result)
                 {
                     userFB = document.toObject(User::class.java)
                 }
             }
             binding.txpUNameEP.setText(userFB?.userName)
             binding.txpNNameEP.setText(userFB?.nickName)
             binding.txpBioEP.setText(userFB?.biography)
             binding.txpDBirthEP.setText(userFB?.dateBirth.toString())
             binding.txpCountryEP.setText(userFB?.country)
             binding.txpRegionEP.setText(userFB?.region)
             Glide
                 .with(this)
                 .load(user.photoUrl)
                 .centerCrop()
                 .placeholder(R.drawable.profile_photo)
                 .into(binding.imvProfile)
         }
    }*/
    private fun updateProfile(name: String, userFB: User){
        val user = auth.currentUser
        if(binding.txpUNameEP.text.toString()==userFB.userName && binding.txpNNameEP.text.toString()==userFB.nickName && binding.txpBioEP.text.toString()==userFB.biography && binding.txpDBirthEP.text.toString()==userFB.dateBirth.toString() && binding.txpCountryEP.text.toString()==userFB.country && binding.txpRegionEP.text.toString()==userFB.region)
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
                        var map: HashMap<String, Any>? = null
                        val userRef = db.collection("users")
                        var countS = 0
                        var countIFs = 0
                        if(binding.txpUNameEP.text.toString()!=userFB.userName)
                        {
                            map!!.put("userName",binding.txpUNameEP.text.toString())
                            userRef.document(user.email.toString()).update(map).addOnSuccessListener {
                                countS++
                            }
                            countIFs++
                        }else if (binding.txpNNameEP.text.toString()!=userFB.nickName)
                        {
                            map!!.put("nickName",binding.txpNNameEP.text.toString())
                            userRef.document(user.email.toString()).update(map).addOnSuccessListener {
                                countS++
                            }
                            countIFs++
                        }else if(binding.txpBioEP.text.toString()!=userFB.biography)
                        {
                            map!!.put("biography",binding.txpBioEP.text.toString())
                            userRef.document(user.email.toString()).update(map).addOnSuccessListener {
                                countS++
                            }
                            countIFs++
                        }else if(binding.txpDBirthEP.text.toString()!=userFB.dateBirth.toString())
                        {
                            map!!.put("dateBirth",binding.txpDBirthEP.text.toString())
                            userRef.document(user.email.toString()).update(map).addOnSuccessListener {
                                countS++
                            }
                            countIFs++
                        }else if(binding.txpCountryEP.text.toString()!=userFB.country)
                        {
                            map!!.put("country",binding.txpCountryEP.text.toString())
                            userRef.document(user.email.toString()).update(map).addOnSuccessListener {
                                countS++
                            }
                            countIFs++
                        }else if(binding.txpRegionEP.text.toString()!=userFB.region)
                        {
                            map!!.put("region",binding.txpRegionEP.text.toString())
                            userRef.document(user.email.toString()).update(map).addOnSuccessListener {
                                countS++
                            }
                            countIFs++
                        }
                        if (countIFs == countS)
                        {
                            Toast.makeText(baseContext,"Los cambios se realizaron correctamente.", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(baseContext,"No todos los cambios se realizaron correctamente. Por favor, intentelo nuevamente más tarde.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }