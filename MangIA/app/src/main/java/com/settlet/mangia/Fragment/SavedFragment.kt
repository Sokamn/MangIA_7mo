package com.settlet.mangia.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.MyRecipesAdapter
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.R

class SavedFragment : Fragment() {
    private lateinit var rcvMySaves: RecyclerView
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
        val myView = inflater.inflate(R.layout.fragment_saved, container, false)
        rcvMySaves = myView.findViewById(R.id.rcvMyRecipesSavedFS)
        //getImages(profileEmail.toString())
        return myView
    }

    private fun getImages(email: String) {
        rcvMySaves.setHasFixedSize(true)
        rcvMySaves.layoutManager = GridLayoutManager(requireActivity(), 3)
        db.collection("saves").document(email).collection("isSaved").get().addOnSuccessListener { documents ->
            documents.forEach { doc ->
                db.collection("recipes").document(doc.id).get().addOnSuccessListener { rec ->
                    val recipe = rec.toObject<Recipe>()
                    listRecipes.add(recipe!!)
                    val adapter = MyRecipesAdapter()
                    rcvMySaves.adapter = adapter
                    adapter.submitList(listRecipes)
                }
            }
        }
    }
}