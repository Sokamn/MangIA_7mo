package com.settlet.mangia

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class IngredientAdapter(private val ingredientList: List<Ingredient>) : RecyclerView.Adapter<IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IngredientViewHolder(layoutInflater.inflate(R.layout.row_ingredient_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val item = ingredientList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = ingredientList.size

    private fun isFollowing(email:String, btn:Button){
        Firebase.firestore
    }
}
