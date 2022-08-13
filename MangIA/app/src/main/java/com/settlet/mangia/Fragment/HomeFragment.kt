package com.settlet.mangia.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.PreviewRecipeAdapter
import com.settlet.mangia.MRecipeStep1Activity
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.SearchActivity
import com.settlet.mangia.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val recipeList = mutableListOf<Recipe>()
    private val followingList = mutableListOf<String>()
    private lateinit var rcvPreviewRecipe: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        rcvPreviewRecipe = binding.rcvPreviewRecipe
        rcvPreviewRecipe.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        rcvPreviewRecipe.layoutManager = linearLayoutManager
        //CheckFollowing()


        binding.bottomBarH.imbScanBB.setOnClickListener {
            Toast.makeText(requireActivity(),"Escanear",Toast.LENGTH_SHORT).show()
        }
        binding.bottomBarH.imbMRecipeBB.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), MRecipeStep1Activity::class.java))
        }
        binding.bottomBarH.imbSearchBB.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(),SearchActivity::class.java))
        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun CheckFollowing(){
        val docRef = db.collection("follow").document(Firebase.auth.currentUser!!.email.toString()).collection("following")
        docRef.addSnapshotListener { value, error ->
            if(error!=null){
                Log.w("TAG","Listen Failed")
                return@addSnapshotListener
            }
            if(value!=null) {
                followingList.clear()
                value.forEach { user ->
                    followingList.add(user.id)
                }
                Log.d("userFollows", followingList.toString())
                ReadRecipes()
            }
        }
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
                val adapter = PreviewRecipeAdapter()
                value.forEach { recipe ->
                    followingList.forEach { userFollowed ->
                        if (recipe["publisher"].toString() == userFollowed) {
                            recipeList.add(recipe.toObject())
                            adapter.submitList(recipeList)
                        }
                    }
                    Log.i("recipeList",recipeList.toString())
                }
                Log.i("recipeList",recipeList.toString())
                rcvPreviewRecipe.adapter = adapter
            }
        }
    }
}

