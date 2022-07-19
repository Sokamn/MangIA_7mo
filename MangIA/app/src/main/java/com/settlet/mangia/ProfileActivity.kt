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
        val defaultPImage = storageReference.child("profilePicture/profile_picture.jpg")
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
            /*db.collection("users").whereEqualTo("email",profileEmail).get().addOnSuccessListener{ documents ->
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
            }*/
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
                    }
                }
            }
        }

        binding.btnEProfileP.setOnClickListener {
            db.collection("users").whereEqualTo("email",profileEmail).get().addOnSuccessListener{ documents ->
                for (document in documents)
                {
                    val followsFB = document.getLong("cantFollows")?.toInt()
                    val followersFB = document.getLong("cantFollowers")?.toInt()
                    val btn = binding.btnEProfileP.text.toString()
                    if(btn=="Editar Perfil"){
                        startActivity(Intent(this,EditProfileActivity::class.java))
                    }else if(btn=="Seguir"){
                        docFollows["isFollowing"] = true.toString()
                        val newFollowers = followersFB!! + 1
                        val newFollows = followsFB!! + 1 //Preguntarle a Juan si usar Firestore estuvo bien o mal
                        //binding.txvFollowsP.text = (binding.txvFollowsP.text.toString().toInt()+1).toString()
                        db.collection("follow").document(Firebase.auth.currentUser!!.email!!.toString()).collection("following").document(profileEmail!!).set(docFollows)
                        db.collection("follow").document(profileEmail).collection("followers").document(Firebase.auth.currentUser!!.email!!.toString()).set(docFollows)
                        db.collection("users").document(profileEmail).update("cantFollowers", newFollowers)
                        db.collection("users").document(Firebase.auth.currentUser!!.email.toString()).update("cantFollows", newFollows)

                    }else if(btn=="Siguiendo"){
                        docFollows["isFollowing"] = false.toString()
                        val newFollowers = followersFB!! - 1
                        val newFollows = followsFB!! - 1
                        //binding.txvFollowsP.text = (binding.txvFollowsP.text.toString().toInt()-1).toString()
                        db.collection("follow").document(Firebase.auth.currentUser!!.email!!.toString()).collection("following").document(profileEmail!!).delete()
                        db.collection("follow").document(profileEmail).collection("followers").document(Firebase.auth.currentUser!!.email!!.toString()).delete()
                        db.collection("users").document(profileEmail).update("cantFollowers", newFollowers)
                        db.collection("users").document(Firebase.auth.currentUser!!.email.toString()).update("cantFollows", newFollows)
                    }
                }
            }
        }
    }

    private fun checkFollow(profileEmail: String) {
        val a = db.collection("follow").document(Firebase.auth.currentUser!!.email!!.toString()).collection("following").document(profileEmail).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                Log.d("DOC",document.exists().toString())
                if (document.exists()){
                    binding.btnEProfileP.text = "Siguiendo"
                }else{
                    binding.btnEProfileP.text = "Seguir"
                }
            }
        }

    }
}