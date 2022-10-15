package com.settlet.mangia

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.UserRateAdapter
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.ActivityUserRateBinding


class UserRateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRateBinding
    private val db = Firebase.firestore
    private val reference = FirebaseDatabase.getInstance().reference
    private val idList = mutableListOf<Array<String>>()
    private var listRate: MutableList<Array<String>> = mutableListOf()
    private lateinit var userAdapter : UserRateAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val recID = intent.getStringExtra("recipeID")
        binding.rcvUsersUR.layoutManager = LinearLayoutManager(this)
        binding.rcvUsersUR.setHasFixedSize(true)
        LoadRates(recID.toString())

        binding.txpSearchUR.doOnTextChanged { text, start, before, count ->
            val usersFiltered = listRate.filter { user -> user[1].lowercase().contains(text.toString().lowercase())||user[2].lowercase().contains(text.toString().lowercase()) }
            if(text==""){
                userAdapter.updateUsers(listRate)
            }else{
                userAdapter.updateUsers(usersFiltered)
            }
        }
        binding.imbBackUR.setOnClickListener {
            onBackPressed()
            this.finish()
        }

    }
    private fun LoadRates(recID: String){
        reference.child("likes").child(recID).get().addOnSuccessListener {
            idList.clear()
            it.children.forEach { userID ->
                idList.add(arrayOf(userID.key.toString(),userID.child("rate").value.toString()))
            }
        }
        showUsers()
    }

    private fun showUsers() {
        userAdapter = UserRateAdapter()
        reference.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listRate.clear()
                snapshot.children.forEach { userValue ->
                    val user = userValue.getValue(User::class.java)
                    if(user!=null){
                        idList.forEach { idFollow ->
                            if(user.userID == idFollow[0]){
                                listRate.add(arrayOf(user.userID,user.userName,user.nickName,idFollow[1]))
                            }
                        }
                    }
                }
                userAdapter.submitList(listRate)
                binding.rcvUsersUR.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}