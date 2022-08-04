package com.settlet.mangia

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.UserRateAdapter
import com.settlet.mangia.databinding.ActivityUserRateBinding


class UserRateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRateBinding
    private val db = Firebase.firestore
    private var listRate: MutableList<Array<String>> = mutableListOf()
    private var listRateFilter: MutableList<Array<String>> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recID = intent.getStringExtra("recipeID")
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
                    listRate.forEach { arrayUserRate ->
                        if(arrayUserRate[1].lowercase().trim().contains(binding.txpSearchUR.text.toString().lowercase().trim())||arrayUserRate[2].lowercase().trim().contains(binding.txpSearchUR.text.toString().lowercase().trim())){
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
        db.collection("likes").document(recID).collection("isLiked").get().addOnSuccessListener{ documents ->
            val adapter = UserRateAdapter()
            binding.rcvUsersUR.layoutManager = LinearLayoutManager(this)
            for (document in documents){
                db.collection("users").document(document.id).get().addOnSuccessListener{ doc ->
                    val docUser = arrayOf(doc.id,doc["userName"].toString(),doc["nickName"].toString(),document["rate"].toString())
                    listRate.add(docUser)
                    binding.rcvUsersUR.adapter = adapter
                    adapter.submitList(listRate)
                }
            }
        }
    }
}