package com.settlet.mangia.ViewHolder

import android.content.Context
import android.content.Intent
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
import com.settlet.mangia.LikeCommentsActivity
import com.settlet.mangia.Model.Comment
import com.settlet.mangia.Model.CustomTypefaceSpan
import com.settlet.mangia.ProfileActivity
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
        var opened = false
        getProfileImage(comment.publisher)
        getTimeLaunch(comment)
        getLikes(comment.likes)
        loadComment(comment.publisher, comment.comment)
        isLiked(comment.commentID)
        if (comment.cantComments == 0){
            binding.vtpAnswerRC.visibility = View.GONE
            binding.txvQuantAnswerRC.visibility = View.GONE
        }else{
            binding.txvQuantAnswerRC.text = if(comment.cantComments == 1) "Ver 1 respuesta" else "ver ${comment.cantComments} respuestas"
            //binding.rcvAnswerCommentsRC.adapter
            binding.vtpAnswerRC.visibility = View.VISIBLE
            binding.txvQuantAnswerRC.visibility = View.VISIBLE
        }

        binding.txvQuantAnswerRC.setOnClickListener {
            if(!opened){
                binding.cstExpandComments.visibility= View.VISIBLE
                opened = true
            }else{
                binding.cstExpandComments.visibility= View.GONE
                opened = false
            }
        }
        binding.txvLikesRC.setOnClickListener {
            val intent = Intent(binding.txvLikesRC.context, LikeCommentsActivity::class.java)
            intent.putExtra("commentID",comment.commentID)
            binding.txvLikesRC.context.startActivity(intent)
        }
        binding.txvAnswerRC.setOnClickListener {

        }
        binding.imvProfilePictureRC.setOnClickListener {
            val intent = Intent(binding.imvProfilePictureRC.context,ProfileActivity::class.java)
            val editor = binding.imvProfilePictureRC.context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileEmail", comment.publisher)
            editor.apply()
            binding.imvProfilePictureRC.context.startActivity(intent)
        }
        binding.imvLikeRC.setOnClickListener {
            val docLiked = hashMapOf<String, Any>()
            if(binding.imvLikeRC.tag.equals("like")){
                docLiked["isLiked"] = true.toString()
                db.collection("likesComments").document(comment.commentID).collection("isLiked")
                    .document(Firebase.auth.currentUser!!.email.toString()).set(docLiked)
                db.collection("comments").document("comments")
                    .collection(comment.recipeID).document(comment.commentID)
                    .update("likes", FieldValue.increment(1))
            }else{
                db.collection("likesComments").document(comment.commentID).collection("isLiked")
                    .document(Firebase.auth.currentUser!!.email.toString()).delete()
                db.collection("comments").document("comments")
                    .collection(comment.recipeID).document(comment.commentID)
                    .update("likes", FieldValue.increment(-1))
            }
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
        val diffSeconds = diffTime.seconds
        val diffMinutes = diffTime.toMinutes()
        val diffHours = diffTime.toHours()
        val diffDays = diffTime.toDays().toFloat()

        if (diffSeconds>=60){
            if(diffMinutes>=60){
                if(diffHours>=24){
                    if(diffDays>7){
                        val diffWeeks = diffDays/7
                        binding.txvTimePostRC.text = "${diffWeeks.roundToInt()} sem"
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