package com.settlet.mangia.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.settlet.mangia.Adapter.MyRecipesAdapter
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.R

class RecipesFragment : Fragment() {

    private lateinit var rcvMyRecipes:RecyclerView
    private val listRecipes = mutableListOf<Recipe>()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val prefs = requireActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val profileEmail = prefs.getString("profileEmail","none")
        val myView = inflater.inflate(R.layout.fragment_recipes, container, false)
        rcvMyRecipes = myView.findViewById(R.id.rcvMyRecipesFR)
        getImages(profileEmail.toString())
        return myView
    }

    private fun getImages(email: String) {
        db.collection("recipes").whereEqualTo("publisher", email).get().addOnSuccessListener { documents ->
            documents.forEach { doc ->
                val recipe = doc.toObject<Recipe>()
                listRecipes.add(recipe)
            }
            rcvMyRecipes.setHasFixedSize(true)
            rcvMyRecipes.layoutManager = GridLayoutManager(requireActivity(), 3)
            val adapter = MyRecipesAdapter()
            rcvMyRecipes.adapter = adapter
            adapter.submitList(listRecipes)
        }
    }
}