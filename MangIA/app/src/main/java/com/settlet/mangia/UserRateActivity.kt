package com.settlet.mangia

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.UserAdapter
import com.settlet.mangia.Adapter.UserRateAdapter
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.ActivityUserRateBinding


class UserRateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRateBinding
    private val db = Firebase.firestore
    private val reference = FirebaseDatabase.getInstance().reference
    private val idList = mutableListOf<Array<String>>()
    private var listRate: MutableList<Array<String>> = mutableListOf()
    private var listRateFilter: MutableList<Array<String>> = mutableListOf()


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
        binding.imbBackUR.setOnClickListener {
            onBackPressed()
            this.finish()
        }
        binding.txpSearchUR.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if(binding.txpSearchUR.text.isNotEmpty()){
                    listRateFilter.clear()
                    val searchedText = binding.txpSearchUR.text.toString().lowercase().trim()
                    listRate.forEach { arrayUserRate ->
                        if(arrayUserRate[1].lowercase().trim().contains(searchedText)||arrayUserRate[2].lowercase().trim().contains(searchedText)){
                            listRateFilter.add(arrayUserRate)
                        }
                    }
                    val adapter = UserRateAdapter()
                    binding.rcvUsersUR.adapter = adapter
                    adapter.submitList(listRateFilter)
                }  else{
                    val adapter = UserRateAdapter()
                    binding.rcvUsersUR.adapter = adapter
                    adapter.submitList(listRate)
                }
            }
        })

    }
    private fun LoadRates(recID: String){
        reference.child("likes").child(recID).get().addOnSuccessListener {
            idList.clear()
            it.children.forEach { userID ->
                idList.add(arrayOf(userID.key.toString(),userID.child("rate").value.toString()))
            }
        }
        showUsers()
        /*db.collection("likes").document(recID).collection("isLiked").get().addOnSuccessListener{ documents ->

            for (document in documents){
                db.collection("users").document(document.id).get().addOnSuccessListener{ doc ->
                    val docUser = arrayOf(doc.id,doc["userName"].toString(),doc["nickName"].toString(),document["rate"].toString())
                    listRate.add(docUser)
                    binding.rcvUsersUR.adapter = adapter
                    adapter.submitList(listRate)
                }
            }
        }*/
    }

    private fun showUsers() {
        val userAdapter = UserRateAdapter()
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