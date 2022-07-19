package com.settlet.mangia.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
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
        isFollowing(item.email,holder.binding.btnFollowRU)

        if (item.email == Firebase.auth.currentUser!!.email)
        {
            holder.binding.btnFollowRU.visibility = View.GONE
        }

        holder.render(item)
    }

    override fun getItemCount(): Int = userList.size


}
private fun isFollowing(email:String, button: Button){
    db.collection("follow").document(Firebase.auth.currentUser!!.email!!.toString()).collection("following").document(email).addSnapshotListener { value, error ->
        if (error!=null) {
            Log.w("TAG","Listen Failed")
            return@addSnapshotListener
        }
        if (value != null) {
            if (value.exists()){
                button.setBackgroundDrawable(getDrawable(button.context,R.drawable.button_profile_following))
                button.setTextColor(getColor(button.context,R.color.white))
                button.text = "Siguiendo"
            }else{
                button.setBackgroundDrawable(getDrawable(button.context, R.drawable.button_profile_follow))
                button.setTextColor(getColor(button.context, R.color.colorButtonFollow))
                button.text = "Seguir"
            }
        }

    }

}