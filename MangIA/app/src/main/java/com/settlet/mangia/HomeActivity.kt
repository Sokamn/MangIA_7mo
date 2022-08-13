package com.settlet.mangia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.bottom_bar.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.nav_header_home.view.*


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fragment: View
    private val reference = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        val homeView = findViewById<View>(R.id.nav_host_fragment_content_home)

        homeView.bottom_barH.imbScanBB.setOnClickListener {
            //Toast.makeText(baseContext,"Escanear",Toast.LENGTH_SHORT).show()
            val editor = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileID", "VjiyGqxeelcjiOoxG5L6THnjqvh2")
            editor.apply()
            val intent = Intent(this, ProfileActivity::class.java)
            this.startActivity(intent)
        }
        homeView.bottom_barH.imbMRecipeBB.setOnClickListener {
            startActivity(Intent(this, MRecipeStep1Activity::class.java))
        }
        homeView.bottom_barH.imbSearchBB.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }


        binding.drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(view: View, v: Float) {
                if (binding.drawerLayout.isDrawerOpen(view)) {
                    window.statusBarColor = getColor(R.color.primaryColor)
                }else{
                    window.statusBarColor = getColor(R.color.lightFont)
                }
            }
            override fun onDrawerOpened(view: View) {
                window.statusBarColor = getColor(R.color.lightFont)
            }
            override fun onDrawerClosed(view: View) {
                window.statusBarColor = getColor(R.color.primaryColor)
            }
            override fun onDrawerStateChanged(i: Int) {}
        })



        binding.btnCSesionH.setOnClickListener {
            logOut()
        }

        binding.navView.nav_view.getHeaderView(0).setOnClickListener {
            val editor = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileID", Firebase.auth.currentUser!!.uid)
            editor.apply()
            val intent = Intent(this, ProfileActivity::class.java)
            this.startActivity(intent)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_notif, R.id.nav_fav, R.id.nav_config
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        val defaultPImage = storageReference.child("profilePicture/profile_picture.jpg")
        if (currentUser!=null)
        {
            val pImageRef = storageReference.child("users/" + currentUser.uid + "/profile.jpg")
            pImageRef.downloadUrl.addOnSuccessListener { result ->
                Glide.with(this)
                    .load(result)
                    .into(nav_view.imvProfileNH)
            }
                .addOnFailureListener {
                    defaultPImage.downloadUrl.addOnSuccessListener { result ->
                        Glide.with(this)
                            .load(result)
                            .into(nav_view.imvProfileNH)
                    }
                }
            reference.child("users").child(currentUser.uid).get().addOnSuccessListener {
                val user = it.getValue(User::class.java)
                if(user!=null){
                    val uNameFB = user.userName
                    val nNameFB = user.nickName

                    val uName = binding.navView.nav_view.getHeaderView(0).findViewById<TextView>(R.id.txvUNameNH)
                    val nName = binding.navView.nav_view.getHeaderView(0).findViewById<TextView>(R.id.txvNNameNH)
                    val follows = binding.navView.nav_view.getHeaderView(0).findViewById<TextView>(R.id.txvFollowsNH)
                    val followers = binding.navView.nav_view.getHeaderView(0).findViewById<TextView>(R.id.txvFollowersNH)

                    uName.text = "@$uNameFB"
                    nName.text = nNameFB
                    getNrFollowsFollowers(currentUser.uid,followers,follows)
                }
            }
            /*db.collection("users").document(currentUser.email.toString()).get().addOnSuccessListener{ document ->
            }*/
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun logOut()
    {
        Firebase.auth.signOut()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun getNrFollowsFollowers(userID: String, textViewFollowers:TextView, textViewFollows:TextView) {
        reference.child("follow").child(userID).child("followers").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                textViewFollowers.text = "${snapshot.childrenCount} Seguidores"
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        reference.child("follow").child(userID).child("following").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                textViewFollows.text = "${snapshot.childrenCount} Seguidos"
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}
