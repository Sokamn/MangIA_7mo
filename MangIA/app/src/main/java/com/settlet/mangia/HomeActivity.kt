package com.settlet.mangia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.activity_home.view.*

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        binding.btnCSesionH.setOnClickListener {
            logOut()
        }

        binding.navView.nav_view.getHeaderView(0).setOnClickListener {
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
        if (currentUser!=null)
        {
            db.collection("users").whereEqualTo("email",currentUser.email.toString()).get().addOnSuccessListener{ documents ->
                for (document in documents)
                {
                    val uNameFB = document.getString("userName").toString()
                    val nNameFB = document.getString("nickName").toString()
                    val followsFB = "${document.getLong("cantFollows")?.toInt()} Seguidos"
                    val followersFB = "${document.getLong("cantFollowers")?.toInt()} Seguidores"

                    val uName = binding.navView.nav_view.getHeaderView(0).findViewById<TextView>(R.id.txvUNameNH)
                    val nName = binding.navView.nav_view.getHeaderView(0).findViewById<TextView>(R.id.txvNNameNH)
                    val follows = binding.navView.nav_view.getHeaderView(0).findViewById<TextView>(R.id.txvFollowsNH)
                    val followers = binding.navView.nav_view.getHeaderView(0).findViewById<TextView>(R.id.txvFollowersNH)

                    uName.text = "@$uNameFB"
                    nName.text = nNameFB
                    follows.text = followsFB
                    followers.text = followersFB

                    Log.d("TAG", "${document.id} => ${document.data}")
                }
            }
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
    }
}
