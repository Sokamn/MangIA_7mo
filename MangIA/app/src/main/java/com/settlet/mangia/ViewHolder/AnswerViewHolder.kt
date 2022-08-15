package com.settlet.mangia.ViewHolder

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.CommentsActivity
import com.settlet.mangia.LikeCommentsActivity
import com.settlet.mangia.Model.Comment
import com.settlet.mangia.Model.CustomTypefaceSpan
import com.settlet.mangia.ProfileActivity
import com.settlet.mangia.R
import com.settlet.mangia.databinding.RowCommentAnswerBinding
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class AnswerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = RowCommentAnswerBinding.bind(view)
    val storageReference = FirebaseStorage.getInstance().reference
    val context = binding.txvAnswerRCA.context as CommentsActivity
    val reference = FirebaseDatabase.getInstance().reference
    val db = Firebase.firestore

    fun render(answer: Comment) {
        val txpAddComment = context.findViewById<EditText>(R.id.txpAddCommentAC)
        val txvAnswerTitle = context.findViewById<TextView>(R.id.txvTitleAnswer)
        val crdAnswer = context.findViewById<CardView>(R.id.crdAnswer)
        val cstAnswer = context.findViewById<ConstraintLayout>(R.id.cstAnswer)
        getProfileImage(answer.publisher)
        getTimeLaunch(answer)
        getLikes(answer.answerID!!)
        isLiked(answer.answerID!!)
        loadComment(answer.publisher,answer.comment)

        binding.txvLikesRCA.setOnClickListener {
            val intent = Intent(binding.txvLikesRCA.context, LikeCommentsActivity::class.java)
            intent.putExtra("commentID",answer.answerID)
            binding.txvLikesRCA.context.startActivity(intent)
        }
        binding.txvAnswerRCA.setOnClickListener {
            reference.child("users").child(answer.publisher).get().addOnSuccessListener {
                crdAnswer.visibility = View.VISIBLE
                txpAddComment.setText("@${it.child("userName").value}")
                cstAnswer.tag = answer.commentID
                txvAnswerTitle.text = "Respondiendo a ${it.child("userName").value}"
                txpAddComment.requestFocus()
                val imm: InputMethodManager = binding.txvAnswerRCA.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
            /*db.collection("users").document(answer.publisher).get().addOnSuccessListener {
                crdAnswer.visibility = View.VISIBLE
                txpAddComment.setText("@${it["userName"]}")
                cstAnswer.tag = answer.commentID
                txvAnswerTitle.text = "Respondiendo a ${it["userName"]}"
                txpAddComment.requestFocus()
                val imm: InputMethodManager = binding.txvAnswerRCA.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }*/
        }
        binding.imvProfilePictureRCA.setOnClickListener {
            val intent = Intent(binding.imvProfilePictureRCA.context, ProfileActivity::class.java)
            val editor = binding.imvProfilePictureRCA.context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileID", answer.publisher)
            editor.apply()
            binding.imvProfilePictureRCA.context.startActivity(intent)
        }
        binding.imvLikeRCA.setOnClickListener {
            if(binding.imvLikeRCA.tag.equals("like")){
                reference.child("likesComments").child(answer.answerID!!).child(Firebase.auth.currentUser!!.uid).setValue(true)
            }else{
                reference.child("likesComments").child(answer.answerID!!).child(Firebase.auth.currentUser!!.uid).removeValue()
            }
            /*val docLiked = hashMapOf<String, Any>()
            if(binding.imvLikeRCA.tag.equals("like")){
                docLiked["isLiked"] = true.toString()
                db.collection("likesComments").document(answer.answerID!!).collection("isLiked")
                    .document(Firebase.auth.currentUser!!.email.toString()).set(docLiked)
                db.collection("comments").document("comments")
                    .collection(answer.recipeID).document(answer.commentID).collection("answers").document(answer.answerID!!)
                    .update("likes", FieldValue.increment(1))
            }else{
                db.collection("likesComments").document(answer.answerID!!).collection("isLiked")
                    .document(Firebase.auth.currentUser!!.email.toString()).delete()
                db.collection("comments").document("comments")
                    .collection(answer.recipeID).document(answer.commentID).collection("answers").document(answer.answerID!!)
                    .update("likes", FieldValue.increment(-1))
            }*/
        }
    }

    private fun loadComment(publisher:String, comment: String) {
        reference.child("users").child(publisher).get().addOnSuccessListener {
            val userName = it.child("userName").value.toString()
            val txtComment = "$userName $comment"
            val ss = SpannableString(txtComment)
            val manjariBold = Typeface.createFromAsset(binding.txvCommentRCA.context.applicationContext.assets, "font/manjaribold.ttf")
            ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, userName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.txvCommentRCA.text = ss
        }
        /*db.collection("users").document(publisher).get().addOnSuccessListener { doc ->
            val userName = doc["userName"].toString()
            val txtComment = "$userName $comment"
            val ss = SpannableString(txtComment)
            val manjariBold = Typeface.createFromAsset(binding.txvCommentRCA.context.applicationContext.assets, "font/manjaribold.ttf")
            ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, userName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.txvCommentRCA.text = ss
        }*/
    }

    private fun isLiked(answerID: String) {
        reference.child("likesComments").child(answerID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(Firebase.auth.currentUser!!.uid).exists()) {
                    binding.imvLikeRCA.setImageResource(R.drawable.ic_unlike_comment)
                    binding.imvLikeRCA.tag = "liked"
                }
                else{
                    binding.imvLikeRCA.setImageResource(R.drawable.ic_like_comment)
                    binding.imvLikeRCA.tag = "like"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        /*val docRef = db.collection("likesComments").document(answerID).collection("isLiked")
            .document(Firebase.auth.currentUser!!.email.toString())
        docRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("TAG", "Listen Failed")
                return@addSnapshotListener
            }
            if (value != null) {
                if (value.exists()) {
                    binding.imvLikeRCA.setImageResource(R.drawable.ic_unlike_comment)
                    binding.imvLikeRCA.tag = "liked"
                }
                else{
                    binding.imvLikeRCA.setImageResource(R.drawable.ic_like_comment)
                    binding.imvLikeRCA.tag = "like"
                }
            }
        }*/
    }

    private fun getLikes(answerID: String) {
        reference.child("likesComments").child(answerID).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount.toString() == "0"){
                    binding.txvLikesRCA.visibility = View.GONE
                }else{
                    binding.txvLikesRCA.visibility = View.VISIBLE
                    binding.txvLikesRCA.text = "${snapshot.childrenCount} Me gusta"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun getTimeLaunch(answer: Comment) {
        val timeLaunch = LocalDateTime.parse(answer.timeLaunch, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val timeNow = LocalDateTime.parse(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val diffTime = Duration.between(timeLaunch,timeNow)
        val diffSeconds = diffTime.seconds
        val diffMinutes = diffTime.toMinutes()
        val diffHours = diffTime.toHours()
        val diffDays = diffTime.toDays()

        if (diffSeconds>=60){
            if(diffMinutes>=60){
                if(diffHours>=24){
                    if(diffDays>7){
                        val diffWeeks = diffTime.toDays().toFloat()/7
                        binding.txvTimePostRCA.text = "${diffWeeks.roundToInt()} sem"
                    }else{
                        binding.txvTimePostRCA.text = "$diffDays d"
                    }
                } else{
                    binding.txvTimePostRCA.text = "$diffHours h"
                }
            } else{
                binding.txvTimePostRCA.text = "$diffMinutes min"
            }
        }else{
            binding.txvTimePostRCA.text = "$diffSeconds seg"
        }
    }

    private fun getProfileImage(publisher: String) {
        val pImageRef = storageReference.child("users/${publisher}/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(binding.imvProfilePictureRCA.context)
                .load(result)
                .into(binding.imvProfilePictureRCA)
        }
    }


}