package com.settlet.mangia.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.PreviewRecipeAdapter
import com.settlet.mangia.Adapter.RecipeSearchedAdapter
import com.settlet.mangia.Adapter.UserAdapter
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.Model.User
import com.settlet.mangia.R

class RecipeSearchedFragment : Fragment() {
    private lateinit var rcvRecipesSearched : RecyclerView
    private val db = Firebase.firestore
    private val recipeList = mutableListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_recipe_searched, container, false)
        rcvRecipesSearched = myView.findViewById(R.id.rcvRecipeSearched)
        rcvRecipesSearched.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        rcvRecipesSearched.layoutManager = linearLayoutManager
        ReadRecipes()
        return myView
    }
    private fun ReadRecipes(){
        val docRef = db.collection("recipes")
        docRef.addSnapshotListener { value, error ->
            recipeList.clear()
            if(error!=null){
                Log.w("TAG","Listen Failed")
                return@addSnapshotListener
            }
            if(value!=null) {
                val adapter = RecipeSearchedAdapter()
                value.forEach { recipe ->
                    recipeList.add(recipe.toObject())
                    adapter.submitList(recipeList)
                }
                rcvRecipesSearched.adapter = adapter
            }
        }
    }


}