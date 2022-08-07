package com.settlet.mangia.ViewHolder

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.Comment
import com.settlet.mangia.Model.CustomTypefaceSpan
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.R
import com.settlet.mangia.databinding.RowCommentBinding
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class CommentViewHolder(view:View): RecyclerView.ViewHolder(view) {
    val binding = RowCommentBinding.bind(view)
    val storageReference = FirebaseStorage.getInstance().reference
    val db = Firebase.firestore

    fun render(comment: Comment) {
        getProfileImage(comment.publisher)
        getTimeLaunch(comment)
        getLikes(comment.likes)
        loadComment(comment.publisher, comment.comment)
        isLiked(comment.commentID)
        binding.txvLikesRC.setOnClickListener {
            val docComment = hashMapOf<String, Any>()
            if (binding.imvLikeRC.tag == "like"){
                docComment["isLiked"] = true.toString()
                db.collection("likesComments").document(comment.commentID).collection("isLiked")
                    .document(Firebase.auth.currentUser!!.email.toString()).set(docComment)
            }else{
                db.collection("likesComments").document(comment.commentID).collection("isLiked")
                    .document(Firebase.auth.currentUser!!.email.toString()).delete()
            }

        }
        binding.txvAnswerRC.setOnClickListener {

        }
        binding.imvProfilePictureRC.setOnClickListener {

        }
        binding.imvLikeRC.setOnClickListener {

        }
    }

    private fun isLiked(commentID: String) {
        val docRef = db.collection("likesComments").document(commentID).collection("isLiked")
            .document(Firebase.auth.currentUser!!.email.toString())
        docRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("TAG", "Listen Failed")
                return@addSnapshotListener
            }
            if (value != null) {
                if (value.exists()) {
                    binding.imvLikeRC.setImageResource(R.drawable.ic_unlike_comment)
                    binding.imvLikeRC.tag = "liked"
                }
                else{
                    binding.imvLikeRC.setImageResource(R.drawable.ic_like_comment)
                    binding.imvLikeRC.tag = "like"
                }
            }
        }
    }

    private fun loadComment(publisher:String, comment: String) {
        db.collection("users").document(publisher).get().addOnSuccessListener { doc ->
            val userName = doc["userName"].toString()
            val txtComment = "$userName $comment"
            val ss = SpannableString(txtComment)
            val manjariBold = Typeface.createFromAsset(binding.txvCommentRC.context.applicationContext.assets, "font/manjaribold.ttf")
            ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, userName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.txvCommentRC.text = ss
        }
    }

    private fun getLikes(likes: Int) {
        if (likes == 0){
            binding.txvLikesRC.visibility = View.GONE
        }else{
            binding.txvLikesRC.visibility = View.VISIBLE
            binding.txvLikesRC.text = "$likes Me gusta"
        }
    }

    private fun getTimeLaunch(comment: Comment) {
        val timeLaunch = LocalDateTime.parse(comment.timeLaunch, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val timeNow = LocalDateTime.parse(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val diffTime = Duration.between(timeLaunch,timeNow)
        val diffSeconds = diffTime.toSeconds()
        val diffMinutes = diffTime.toMinutes()
        val diffHours = diffTime.toHours()
        val diffDays = diffTime.toDays().toFloat()

        if (diffSeconds>=60){
            if(diffMinutes>=60){
                if(diffHours>=24){
                    if(diffDays>7){
                        val diffWeeks = diffDays/7
                        diffWeeks.roundToInt()
                        binding.txvTimePostRC.text = "$diffWeeks sem"
                    }else{
                        binding.txvTimePostRC.text = "$diffDays d"
                    }
                } else{
                    binding.txvTimePostRC.text = "$diffHours h"
                }
            } else{
                binding.txvTimePostRC.text = "$diffMinutes min"
            }
        }else{
            binding.txvTimePostRC.text = "$diffSeconds seg"
        }
    }

    private fun getProfileImage(email: String) {
        val pImageRef = storageReference.child("users/${email}/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(binding.imvProfilePictureRC.context)
                .load(result)
                .into(binding.imvProfilePictureRC)
        }
    }

}