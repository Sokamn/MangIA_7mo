package com.settlet.mangia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
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
import com.settlet.mangia.Provider.ObjectDetectorHelper
import com.settlet.mangia.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.app_bar_home.view.*
import kotlinx.android.synthetic.main.bottom_bar.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.tensorflow.lite.task.vision.detector.Detection
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fragment: View
    private val reference = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference
    private lateinit var photoFile: File
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            val imageBitmap = BitmapFactory.decodeFile(photoFile.path)
            val detector = ObjectDetectorHelper(
                context = this,
                objectDetectorListener =
                object : ObjectDetectorHelper.DetectorListener {
                    override fun onError(error: String) {}

                    override fun onResults(
                        results: MutableList<Detection>?,
                        inferenceTime: Long,
                        imageHeight: Int,
                        imageWidth: Int
                    ) {
                        if(results == null){
                            Log.d("SCAN", "No se detecto naranjas")
                        }else{
                            for (result in results) {

                            }
                        }
                    }
                }
            )
            detector.detect(imageBitmap,0)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        val homeView = findViewById<View>(R.id.nav_host_fragment_content_home)

        app_bar_home.imvSearchAB.setOnClickListener {
            val intent = Intent(this,SearchActivity::class.java)
            this.startActivity(intent)
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
            override fun onDrawerStateChanged(i: Int) {

            }
        })

        binding.appBarHome.imvOptionsABH.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.appBarHome.imvMKRecipe.setOnClickListener {
            this.startActivity(Intent(this,MRecipeStep1Activity::class.java))
        }

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

    private fun takePicture() {
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createPhotoFile()
        val fileProvider = FileProvider.getUriForFile(this, "com.settlet.mangia.fileprovider",photoFile)
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
        startForResult.launch(intentCamera)
    }

    private fun createPhotoFile(): File {
        val timeStamp:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}",".jpg",storageDir.apply {

        })
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

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun logOut()
    {
        Firebase.auth.signOut()
        val intent = Intent(this,StartActivity::class.java)
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
