package com.settlet.mangia.Fragment

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.settlet.mangia.Adapter.UserAdapter
import com.settlet.mangia.Model.User
import com.settlet.mangia.R

class UserSearchedFragment : Fragment() {
    private lateinit var rcvUserSearched: RecyclerView
    private lateinit var txpSearch: EditText
    private lateinit var vwpSearch: ViewPager2
    private val reference = FirebaseDatabase.getInstance().reference
    private val userList = mutableListOf<com.settlet.mangia.Model.User>()
    private val userAdapter = UserAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_user_searched, container, false)
        showUsers()
        rcvUserSearched = myView.findViewById(R.id.rcvUserSearched)
        txpSearch = requireActivity().findViewById(R.id.txpSearchAS)
        rcvUserSearched.layoutManager = LinearLayoutManager(requireActivity())
        rcvUserSearched.adapter = userAdapter
        vwpSearch = requireActivity().findViewById(R.id.vwpContentAS)
        txpSearch.doOnTextChanged { text, start, before, count ->
            val usersFiltered = userList.filter { user ->
                user.userName.lowercase().contains(text.toString().lowercase())
            }
            if (text == "") {
                userAdapter.updateUsers(mutableListOf())
            } else {
                userAdapter.updateUsers(usersFiltered)
            }
        }
        return myView
    }

    private fun showUsers() {
        reference.child("users").get().addOnSuccessListener { userSnap ->
            userList.clear()
            userSnap.children.forEach { userValue ->
                val user = userValue.getValue(User::class.java)
                if (user != null) {
                    userList.add(user)
                }
            }
        }
    }
}