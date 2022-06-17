package com.settlet.mangia.Adapter

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.R
import com.settlet.mangia.Model.User
import com.settlet.mangia.ViewHolder.UserViewHolder
private val db = Firebase.firestore
class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(layoutInflater.inflate(R.layout.row_ingredient_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = userList[position]
        holder.render(item)
        isFollowing(item.email,holder.binding.btnFollowRU)

        if (item.email.equals(Firebase.auth.currentUser!!.email))
        {
            holder.binding.btnFollowRU.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int = userList.size
}
private fun isFollowing(email:String, button: Button){
    val col = db.collection("Follow").document(Firebase.auth.currentUser!!.email!!.toString()).collection("Following")
    col.document(email).get().addOnSuccessListener { document ->
        if (document.exists())
        {
            button.text = "Siguiendo"
        }else{
            button.text = "Seguir"
        }
    }
}