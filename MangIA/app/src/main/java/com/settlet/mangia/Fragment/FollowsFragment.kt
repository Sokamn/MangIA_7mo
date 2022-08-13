package com.settlet.mangia.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.UserAdapter
import com.settlet.mangia.Model.User
import com.settlet.mangia.R

class FollowsFragment : Fragment() {
    private val reference = FirebaseDatabase.getInstance().reference
    private val db = Firebase.firestore
    private val userList = mutableListOf<com.settlet.mangia.Model.User>()
    private lateinit var rcvFollows: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_follows, container, false)
        val prefs = requireActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val profileID = prefs.getString("profileID","none")
        rcvFollows = myView.findViewById(R.id.rcvUsersFollows)
        rcvFollows.setHasFixedSize(true)
        rcvFollows.layoutManager = LinearLayoutManager(requireActivity())
        val currentUser = Firebase.auth.currentUser
        if (currentUser!=null)
        {
            val userAdapter = UserAdapter()
            rcvFollows.adapter = userAdapter
            reference.child("follow").child(profileID!!).child("following").get().addOnSuccessListener {
                userList.clear()
                it.children.forEach { userID ->
                    reference.child("users").child(userID.key.toString()).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            userList.clear()
                            snapshot.children.forEach { userValue ->
                                val user = userValue.getValue(User::class.java)
                                if(user!=null){
                                    userList.add(user)
                                    userAdapter.submitList(userList)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
                }

            }
            /*db.collection("follow").document(profileEmail!!).collection("following").get().addOnSuccessListener{ documents ->
                userList.clear()
                for (document in documents){
                    db.collection("users").document(document.id).addSnapshotListener { value, error ->
                        if (error!=null) {
                            Log.w("TAG","Listen Failed")
                            return@addSnapshotListener
                        }
                        if (value != null) {
                            if (value.exists()){
                                val us: com.settlet.mangia.Model.User = value.toObject()!!
                                userList.add(us)
                                userAdapter.submitList(userList)
                            }
                        }
                    }
                }
            }*/
        }
        return myView
    }
}