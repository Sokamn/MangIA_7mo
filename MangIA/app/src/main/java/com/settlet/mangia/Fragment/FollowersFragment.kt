package com.settlet.mangia.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Adapter.UserAdapter
import com.settlet.mangia.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val storageReference = FirebaseStorage.getInstance().reference
    private val prefs = requireActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE)
    private val profileEmail = prefs.getString("profileEmail","none")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userList = mutableListOf<com.settlet.mangia.Model.User>()
        binding.rcvUsersFollowers.setHasFixedSize(true)
        binding.rcvUsersFollowers.layoutManager = LinearLayoutManager(requireActivity())
        val currentUser = Firebase.auth.currentUser
        if (currentUser!=null)
        {
            db.collection("follow").document(profileEmail!!).collection("followers").get().addOnSuccessListener{ documents ->
                for (document in documents){
                    userList.add(document.toObject<com.settlet.mangia.Model.User>())
                }
                val userAdapter = UserAdapter(userList)
                binding.rcvUsersFollowers.adapter = userAdapter
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}