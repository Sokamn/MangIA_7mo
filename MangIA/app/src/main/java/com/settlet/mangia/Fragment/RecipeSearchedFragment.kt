package com.settlet.mangia.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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
    private lateinit var txpSearch: EditText
    private lateinit var vwpSearch: ViewPager2
    private lateinit var prbSearched: ProgressBar
    private val reference = FirebaseDatabase.getInstance().reference
    private val recipeAdapter = RecipeSearchedAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_recipe_searched, container, false)
        rcvRecipesSearched = myView.findViewById(R.id.rcvRecipeSearched)
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        rcvRecipesSearched.layoutManager = linearLayoutManager
        rcvRecipesSearched.adapter = recipeAdapter
        txpSearch = requireActivity().findViewById(R.id.txpSearchAS)
        vwpSearch = requireActivity().findViewById(R.id.vwpContentAS)
        loadRecipes()
        prbSearched = requireActivity().findViewById<ProgressBar>(R.id.prbSearched)
        txpSearch.doOnTextChanged { text, start, before, count ->
            val usersFiltered = recipeList.filter { recipe ->
                prbSearched.visibility = View.VISIBLE
                recipe.title.lowercase().contains(text.toString().lowercase())
            }
            if (text != null) {
                if (text.isEmpty()) {
                    recipeAdapter.updateRecipes(mutableListOf())
                    prbSearched.visibility = View.GONE

                } else {
                    recipeAdapter.updateRecipes(usersFiltered)
                    prbSearched.visibility = View.GONE

                }
            }
            else {
                recipeAdapter.updateRecipes(usersFiltered)
                prbSearched.visibility = View.GONE

            }
        }
        return myView
    }

    private fun loadRecipes() {
        db.collection("recipes").get().addOnSuccessListener { snapshot->
            recipeList.clear()
            prbSearched.visibility = View.VISIBLE
            snapshot.forEach { recipe ->
                val recipeOb: Recipe = recipe.toObject()
                recipeList.add(recipeOb)
            }
            prbSearched.visibility = View.GONE
        }
    }
}