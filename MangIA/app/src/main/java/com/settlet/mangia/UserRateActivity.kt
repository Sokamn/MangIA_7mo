package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.IngredientAdapter
import com.settlet.mangia.Adapter.UserRateAdapter
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.ActivityUserRateBinding
import kotlinx.android.synthetic.main.activity_home.view.*

class UserRateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRateBinding
    private val db = Firebase.firestore
    private var listRate: MutableList<Array<String>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recID = intent.getStringExtra("recipeID")
        db.collection("likes").document(recID.toString()).collection("isLiked").get().addOnSuccessListener{ documents ->
            for (document in documents){
                val a = arrayOf(document.id,document["rate"].toString())
                listRate.add(a)
            }
            binding.rcvUsersUR.layoutManager = LinearLayoutManager(this)
            val adapter = UserRateAdapter()
            binding.rcvUsersUR.adapter = adapter
            adapter.submitList(listRate)
            // LANZAR EL ADAPTER (ANALIZAR SI DEBE SER SNAPSHOT O SOLAMENTE CARGAR DATOS; ESTO DEPENDE DE SI SE VA A PODER SEGUIR AL USUARIO DESDE ESTA SECCION O NO.)
        }
    }

}