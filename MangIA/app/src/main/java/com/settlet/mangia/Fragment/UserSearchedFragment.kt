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
    private val reference = FirebaseDatabase.getInstance().reference
    private val userList = mutableListOf<com.settlet.mangia.Model.User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_user_searched, container, false)
        rcvUserSearched = myView.findViewById(R.id.rcvUserSearched)
        txpSearch = requireActivity().findViewById(R.id.txpSearchAS)
        rcvUserSearched.setHasFixedSize(true)
        rcvUserSearched.layoutManager = LinearLayoutManager(requireActivity())
        txpSearch.doOnTextChanged { text, start, before, count ->
            showUsers(txpSearch.text.toString())
        }
        return myView
    }

    private fun showUsers(textSearched: String) {
        val userAdapter = UserAdapter()
        rcvUserSearched.adapter = userAdapter
        reference.child("users").orderByChild("userName").startAt(textSearched).endAt("\uf8ff").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                snapshot.children.forEach { userValue ->
                    val user = userValue.getValue(User::class.java)
                    if(user!=null){
                        userList.add(user)
                    }
                }
                userAdapter.submitList(userList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}