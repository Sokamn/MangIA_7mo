package com.settlet.mangia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.settlet.mangia.databinding.ActivityCheckMailBinding
import com.settlet.mangia.databinding.ActivityLoginBinding
import com.settlet.mangia.databinding.ActivityProfileBinding
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_home.view.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val storageReference = FirebaseStorage.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imbBackP.setOnClickListener {
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
            //PROFILE ACTIVITY LINEA 39 COMO VOY A UN INTENT EXISTENTE
        }
        binding.btnEProfileP.setOnClickListener {
            val intent = Intent(this,EditProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        val defaultPImage = storageReference.child("profilePicture/profile_picture.jpg")
        if (currentUser!=null)
        {
            val pImageRef = storageReference.child("users/" + FirebaseAuth.getInstance().currentUser!!.uid + "/profile.jpg")
            pImageRef.downloadUrl.addOnSuccessListener { result ->
                Glide.with(this)
                    .load(result)
                    .into(binding.imvProfileP)

            }
                .addOnFailureListener {
                    defaultPImage.downloadUrl.addOnSuccessListener { result ->
                    Glide.with(this)
                        .load(result)
                        .into(binding.imvProfileP)
                    }
                }
            db.collection("users").whereEqualTo("email",currentUser.email.toString()).get().addOnSuccessListener{ documents ->
                for (document in documents)
                {
                    val uNameFB = document.getString("userName").toString()
                    val nNameFB = document.getString("nickName").toString()
                    val followsFB = "${document.getLong("cantFollows")?.toInt()}\nSeguidos"
                    val followersFB = "${document.getLong("cantFollowers")?.toInt()}\nSeguidores"
                    val cantRecipesFB = "${document.getLong("cantRecipes")?.toInt()}\nRecetas"
                    val bioFB = document.getString("biography").toString()


                    binding.txvUNameP.text = uNameFB
                    binding.txvNNameP.text = nNameFB
                    binding.txvFollowersP.text = followersFB
                    binding.txvFollowsP.text = followsFB
                    binding.txvRecipesP.text = cantRecipesFB
                    binding.txvBioP.text = bioFB

                    Log.d("TAG", "${document.id} => ${document.data}")
                }
            }
        }
    }
}