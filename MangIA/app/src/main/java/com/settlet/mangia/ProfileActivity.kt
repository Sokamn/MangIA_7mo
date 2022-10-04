package com.settlet.mangia

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Adapter.PagerAdapterP
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val reference = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.secundaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        viewPager = findViewById(R.id.vwpContentP)
        tabLayout = findViewById(R.id.tblTabLayoutP)
        viewPager.adapter = PagerAdapterP(this)
        TabLayoutMediator(tabLayout,viewPager){ tab,position->
            tab.icon = when(position){
                0 -> ContextCompat.getDrawable(this, R.drawable.ic_my_recipes)
                1 -> ContextCompat.getDrawable(this, R.drawable.ic_save_menu)
                else -> throw Resources.NotFoundException("Position Not Found")
            }
        }.attach()

        binding.imbBackP.setOnClickListener {
            val editor = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileID", intent.getStringExtra("preProfileID"))
            editor.apply()
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

    public override fun onBackPressed() {
        super.onBackPressed()
        val editor = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
        editor.putString("profileID", intent.getStringExtra("preProfileID"))
        editor.apply()
    }

    public override fun onStart() {
        super.onStart()
        val prefs = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val profileID = prefs.getString("profileID","none")
        val currentUser = Firebase.auth.currentUser
        if (profileID!=null)
        {
            if(profileID == currentUser!!.uid){
                Glide.with(this)
                    .load(R.drawable.btn_edit_profile)
                    .into(binding.btnEProfileP)
            }else{
                checkFollow(profileID)
            }

            val pImageRef = storageReference.child("users/$profileID/profile.jpg")
            Log.d("PROFI",profileID)
            pImageRef.downloadUrl.addOnSuccessListener { result ->
                Glide.with(this)
                    .load(result)
                    .into(binding.imvProfileP)
            }
            getUserInfo(profileID)
            getNrFollowsFollowers(profileID)
            getNrRecipes(profileID)
        }

        binding.btnEProfileP.setOnClickListener {
            startActivity(Intent(this,EditProfileActivity::class.java))
        }
    }

    private fun checkFollow(profileID: String) {
        reference.child("follow").child(Firebase.auth.currentUser!!.uid).child("following").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(profileID).exists()){
                    Glide.with(this@ProfileActivity)
                        .load(R.drawable.btn_unfollow)
                        .into(binding.btnEProfileP)
                }else{
                    Glide.with(this@ProfileActivity)
                        .load(R.drawable.btn_follow)
                        .into(binding.btnEProfileP)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getNrRecipes(userID: String) {
        reference.child("recipes").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var i = 0
                snapshot.children.forEach { recipeSnapshot ->
                    val recipe = recipeSnapshot.getValue(Recipe::class.java)
                    if(recipe!=null){
                        if(recipe.publisher==userID){
                            i++
                        }
                    }
                }
                binding.txvRecipesP.text = "$i\nRecetas"
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getNrFollowsFollowers(userID: String) {
        reference.child("follow").child(userID).child("followers").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txvFollowersP.text = "${snapshot.childrenCount}\nSeguidores"
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        reference.child("follow").child(userID).child("following").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txvFollowsP.text = "${snapshot.childrenCount}\nSeguidos"
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getUserInfo(userID: String) {
        val docRef = reference.child("users").child(userID)
        docRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    binding.txvUNameP.text = "@"+user.userName
                    binding.txvNNameP.text = user.nickName
                    binding.txvBioP.text = user.biography
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}