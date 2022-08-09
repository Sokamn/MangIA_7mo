package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.UserAdapter
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.ActivityLikeCommentsBinding

class LikeCommentsActivity : AppCompatActivity() {
    private val listLikes = mutableListOf<String>()
    private val userList = mutableListOf<User>()
    private lateinit var binding: ActivityLikeCommentsBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikeCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val commentID = intent.getStringExtra("commentID")
        binding.rcvUsersALC.setHasFixedSize(true)
        binding.rcvUsersALC.layoutManager = LinearLayoutManager(this)
        db.collection("likesComments").document(commentID!!).collection("isLiked").get().addOnSuccessListener { documents ->
            userList.clear()
            for (document in documents){
                db.collection("users").document(document.id).addSnapshotListener { value, error ->
                    val userAdapter = UserAdapter()
                    if (error!=null) {
                        Log.w("TAG","Listen Failed")
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        binding.rcvUsersALC.adapter = userAdapter
                        val us: User = value.toObject()!!
                        userList.add(us)
                        userAdapter.submitList(userList)
                    }
                }
            }
        }

        binding.imbBackALC.setOnClickListener {
            onBackPressed()
            finish()
        }

    }
}