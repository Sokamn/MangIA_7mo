package com.settlet.mangia.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.R
import com.settlet.mangia.Model.User
import com.settlet.mangia.ViewHolder.UserViewHolder


class UserAdapter: ListAdapter<User, UserViewHolder>(DiffCallBack){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_user,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        isFollowing(item.userID,holder.binding.btnFollowRU)

        if (item.userID == Firebase.auth.currentUser!!.uid)
        {
            holder.binding.btnFollowRU.visibility = View.GONE
        }

        holder.render(item)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userID == newItem.userID
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
private fun isFollowing(userID:String, button: Button){
    val reference = FirebaseDatabase.getInstance().reference
    reference.child("follow").child(Firebase.auth.currentUser!!.uid).child("following").addValueEventListener(object:
        ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.child(userID).exists()){
                button.setBackgroundDrawable(getDrawable(button.context, R.drawable.button_profile_follow))
                button.setTextColor(getColor(button.context, R.color.colorButtonFollow))
                button.text = "Siguiendo"
            }else{
                button.setBackgroundDrawable(getDrawable(button.context,R.drawable.button_profile_following))
                button.setTextColor(getColor(button.context,R.color.white))
                button.text = "Seguir"
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }
    })


}