package com.settlet.mangia.Fragment

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.PreviewRecipeAdapter
import com.settlet.mangia.MRecipeStep1Activity
import com.settlet.mangia.Model.CustomTypefaceSpan
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.R
import com.settlet.mangia.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.bottom_bar.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val recipeList = mutableListOf<Recipe>()
    private val followingList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.rcvIngredients.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.rcvIngredients.layoutManager = linearLayoutManager
        binding.rcvIngredients.adapter = PreviewRecipeAdapter(requireActivity(),recipeList)
        recipeList.clear()
        CheckFollowing()


        binding.bottomBarH.imbScanBB.setOnClickListener {
            Toast.makeText(it.context,"Escanear",Toast.LENGTH_SHORT).show()
        }
        binding.bottomBarH.imbMRecipeBB.setOnClickListener {
            val intent = Intent(it.context, MRecipeStep1Activity::class.java)
            this.startActivity(intent)
        }
        binding.bottomBarH.imbSearchBB.setOnClickListener {
            Toast.makeText(it.context,"Buscar",Toast.LENGTH_SHORT).show()
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
                ReadRecipes()
            }
        }
    }

    private fun ReadRecipes(){
        val docRef = db.collection("recipes")
        docRef.addSnapshotListener { value, error ->
            if(error!=null){
                Log.w("TAG","Listen Failed")
                return@addSnapshotListener
            }
            if(value!=null) {
                value.forEach { recipe ->
                    followingList.forEach { userFollowed ->
                        if (recipe["publisher"] == userFollowed) {
                            recipeList.add(recipe.toObject())
                        }
                    }
                }
                binding.rcvIngredients.adapter!!.notifyDataSetChanged()
            }
        }
    }
}

