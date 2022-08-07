package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.databinding.ActivityCommentsBinding
import java.time.LocalDateTime

class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding
    private var listComments = mutableListOf<String>()
    private var db = Firebase.firestore
    private val user = Firebase.auth.currentUser!!
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setImageProfile()
        val recID = intent.getStringExtra("recipeID").toString()


        binding.imbBackAC.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.txvPostCommentAC.setOnClickListener {
            if(binding.txpAddCommentAC.text.isEmpty()){
                Toast.makeText(this,"No puedes enviar un mensaje vacio.",Toast.LENGTH_SHORT).show()
            }else{
                addComment(recID)
            }
        }
    }

    private fun setImageProfile() {
        val pImageRef = storageReference.child("users/${user.email}/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(this)
                .load(result)
                .into(binding.imvProfilePictureAC)
        }
    }

    private fun addComment(recipeID: String) {
        val docComment = hashMapOf<String, Any>()
        docComment["comment"] = binding.txpAddCommentAC.text.toString()
        docComment["publisher"] = user.email.toString()
        docComment["likes"] = 0
        docComment["timeLaunch"] = LocalDateTime.now()
        db.collection("comments").document("comments").collection(recipeID).document().set(docComment).addOnSuccessListener {
            db.collection("recipes").document(recipeID).update("cantComments", FieldValue.increment(1))
        }
        binding.txpAddCommentAC.setText("")
    }
}