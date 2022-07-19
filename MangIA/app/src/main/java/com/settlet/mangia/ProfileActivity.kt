package com.settlet.mangia

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.settlet.mangia.Adapter.PagerAdapterFF
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
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var cantFollowsActual = 0
    private var cantFollowersActual = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = findViewById(R.id.vwpContentP)
        tabLayout = findViewById(R.id.tblTabLayoutP)
        viewPager.adapter = PagerAdapterFF(this)
        TabLayoutMediator(tabLayout,viewPager){ tab,position->
            tab.text = when(position){
                0 -> "Recetas"
                1 -> "Guardados"
                else -> throw Resources.NotFoundException("Position Not Found")
            }
        }.attach()


        binding.imbBackP.setOnClickListener {
            onBackPressed()
            finish()
        }
        binding.txvFollowersP.setOnClickListener {
            val intent = Intent(this,FollowsAndFollowersActivity::class.java)
            intent.putExtra("vPage", "Followers")
            startActivity(intent)
        }
        binding.txvFollowsP.setOnClickListener {
            val intent = Intent(this,FollowsAndFollowersActivity::class.java)
            intent.putExtra("vPage", "Follows")
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val prefs = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val profileEmail = prefs.getString("profileEmail","none")
        val docFollows = hashMapOf<String, Any>()
        val currentUser = Firebase.auth.currentUser
        if (profileEmail!=null)
        {
            if(profileEmail == currentUser!!.email){
                binding.btnEProfileP.text = "Editar Perfil"
            }else{
                checkFollow(profileEmail)
            }

            val pImageRef = storageReference.child("users/$profileEmail/profile.jpg")
            Log.d("PROFI",profileEmail)
            pImageRef.downloadUrl.addOnSuccessListener { result ->
                Glide.with(this)
                    .load(result)
                    .into(binding.imvProfileP)

            }
            val docRef = db.collection("users").document(profileEmail)
            docRef.addSnapshotListener { value, error ->
                if(error!=null){
                    Log.w("TAG","Listen Failed")
                    return@addSnapshotListener
                }
                if(value!=null && value.exists()) {
                    val user = value.toObject<com.settlet.mangia.Model.User>()
                    if (user != null) {
                        binding.txvUNameP.text = user.userName
                        binding.txvNNameP.text = user.nickName
                        binding.txvFollowersP.text = "${user.cantFollowers}\nSeguidores"
                        binding.txvFollowsP.text = "${user.cantFollows}\nSeguidos"
                        binding.txvRecipesP.text = "${user.cantRecipes}\nRecetas"
                        binding.txvBioP.text = user.biography
                        cantFollowsActual = user.cantFollows
                        cantFollowersActual = user.cantFollowers
                    }
                }
            }
        }

        binding.btnEProfileP.setOnClickListener {
            when (binding.btnEProfileP.text.toString()) {
                "Editar Perfil" -> {
                    startActivity(Intent(this,EditProfileActivity::class.java))
                }
                "Seguir" -> {
                    docFollows["isFollowing"] = true.toString()
                    val newFollowers = cantFollowersActual + 1
                    val newFollows = cantFollowsActual + 1
                    db.collection("follow").document(Firebase.auth.currentUser!!.email.toString()).collection("following").document(profileEmail.toString()).set(docFollows)
                    db.collection("follow").document(profileEmail.toString()).collection("followers").document(Firebase.auth.currentUser!!.email!!.toString()).set(docFollows)
                    db.collection("users").document(profileEmail.toString()).update("cantFollowers", FieldValue.increment(1))
                    db.collection("users").document(Firebase.auth.currentUser!!.email.toString()).update("cantFollows", FieldValue.increment(1))
                }
                "Siguiendo" -> {
                    docFollows["isFollowing"] = false.toString()
                    Log.d("FOLLOWS", cantFollowsActual.toString())
                    Log.d("FOLLOWERS", cantFollowsActual.toString())

                    val newFollowers = cantFollowersActual - 1
                    val newFollows = cantFollowsActual - 1
                    db.collection("follow").document(Firebase.auth.currentUser!!.email.toString()).collection("following").document(
                        profileEmail.toString()
                    ).delete()
                    db.collection("follow").document(profileEmail.toString()).collection("followers").document(Firebase.auth.currentUser!!.email!!.toString()).delete()
                    db.collection("users").document(profileEmail.toString()).update("cantFollowers", FieldValue.increment(-1))
                    db.collection("users").document(Firebase.auth.currentUser!!.email.toString()).update("cantFollows", FieldValue.increment(-1))
                }
            }

        }
    }


    private fun checkFollow(profileEmail: String) {
        db.collection("follow").document(Firebase.auth.currentUser!!.email!!.toString()).collection("following").document(profileEmail).addSnapshotListener { value, error ->
            if (error!=null) {
                Log.w("TAG","Listen Failed")
                return@addSnapshotListener
            }
            if (value != null) {
                if (value.exists()){
                    binding.btnEProfileP.text = "Siguiendo"
                }else{
                    binding.btnEProfileP.text = "Seguir"
                }
            }

        }

    }
}