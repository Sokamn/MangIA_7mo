package com.settlet.mangia.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.*
import com.settlet.mangia.Adapter.PreviewRecipeAdapter
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val actualUserID = Firebase.auth.currentUser!!.uid
    private val reference = FirebaseDatabase.getInstance().reference
    private val recipeList = mutableListOf<Recipe>()
    private val followingList = mutableListOf<String>()
    private lateinit var rcvPreviewRecipe: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rcvPreviewRecipe = binding.rcvPreviewRecipe
        rcvPreviewRecipe.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        rcvPreviewRecipe.layoutManager = linearLayoutManager
        CheckFollowing()
        binding.bottomNav.add(MeowBottomNavigation.Model(0,R.drawable.ic_home_menu))
        binding.bottomNav.add(MeowBottomNavigation.Model(1,R.drawable.ic_scan_nav))
        binding.bottomNav.add(MeowBottomNavigation.Model(2,R.drawable.ic_comment_recipe_black))
        binding.bottomNav.show(0,true)

        configSwipe()

        binding.fbtnMRecipe.setOnClickListener {
            this.startActivity(Intent(requireActivity(),MRecipeStep1Activity::class.java))
        }

        binding.bottomNav.setOnClickMenuListener {
            when(it.id){
                0->{

                }
                1->{
                    requireActivity().startActivity(Intent(requireActivity(), ScanCameraActivity::class.java))
                    requireActivity().finish()
                }
                2->{
                    requireActivity().startActivity(Intent(requireActivity(), ChatActivity::class.java))
                    requireActivity().finish()
                }
                else->{

                }
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            showData()
        },2000)



        return root
    }

    private fun configSwipe() {
        binding.swipeHome.setOnRefreshListener {
            binding.shimmerViewFH.visibility = View.VISIBLE
            binding.rcvPreviewRecipe.visibility = View.INVISIBLE
            ReadRecipes()
            Handler(Looper.getMainLooper()).postDelayed({
                showData()
                binding.swipeHome.isRefreshing = false
            },2000)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNav.show(0,true)
    }

    private fun showData() {
        binding.shimmerViewFH.visibility = View.GONE
        binding.rcvPreviewRecipe.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun CheckFollowing(){
        reference.child("follow").child(actualUserID).child("following").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                followingList.clear()
                snapshot.children.forEach { userID ->
                    followingList.add(userID.key.toString())
                    Log.d("followingList", followingList.toString())
                }
                ReadRecipes()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
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

