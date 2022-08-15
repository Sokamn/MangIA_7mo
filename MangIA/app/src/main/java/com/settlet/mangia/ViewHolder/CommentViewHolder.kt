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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.protobuf.Value
import com.settlet.mangia.Adapter.AnswerAdapter
import com.settlet.mangia.CommentsActivity
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
    val reference = FirebaseDatabase.getInstance().reference
    val db = Firebase.firestore
    private val answerList = mutableListOf<Comment>()
    val a = binding.txvAnswerRC.context as CommentsActivity

    fun render(comment: Comment) {
        val txpAddComment = a.findViewById<EditText>(R.id.txpAddCommentAC)
        val txvAnswerTitle = a.findViewById<TextView>(R.id.txvTitleAnswer)
        val crdAnswer = a.findViewById<CardView>(R.id.crdAnswer)
        val cstAnswer = a.findViewById<ConstraintLayout>(R.id.cstAnswer)
        getProfileImage(comment.publisher)
        getTimeLaunch(comment)
        getLikes(comment.commentID)
        getAnswers(comment)
        loadComment(comment.publisher, comment.comment)
        isLiked(comment.commentID)

        binding.txvLikesRC.setOnClickListener {
            val intent = Intent(binding.txvLikesRC.context, LikeCommentsActivity::class.java)
            intent.putExtra("commentID",comment.commentID)
            binding.txvLikesRC.context.startActivity(intent)
        }
        binding.txvAnswerRC.setOnClickListener {
            reference.child("users").child(comment.publisher).get().addOnSuccessListener {
                crdAnswer.visibility = View.VISIBLE
                txpAddComment.setText("@${it.child("userName").value}")
                cstAnswer.tag = comment.commentID
                txvAnswerTitle.text = "Respondiendo a ${it.child("userName").value}"
                txpAddComment.requestFocus()
                val imm: InputMethodManager = binding.txvAnswerRC.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
            /*db.collection("users").document(comment.publisher).get().addOnSuccessListener {
                crdAnswer.visibility = View.VISIBLE
                txpAddComment.setText("@${it["userName"]}")
                cstAnswer.tag = comment.commentID
                txvAnswerTitle.text = "Respondiendo a ${it["userName"]}"
                txpAddComment.requestFocus()
                val imm: InputMethodManager = binding.txvAnswerRC.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }*/
        }
        binding.imvProfilePictureRC.setOnClickListener {
            val intent = Intent(binding.imvProfilePictureRC.context,ProfileActivity::class.java)
            val editor = binding.imvProfilePictureRC.context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileEmail", comment.publisher)
            editor.apply()
            binding.imvProfilePictureRC.context.startActivity(intent)
        }
        binding.imvLikeRC.setOnClickListener {
            //val docLiked = hashMapOf<String, Any>()
            if(binding.imvLikeRC.tag.equals("like")){
                //docLiked["isLiked"] = true.toString()
                reference.child("likesComments").child(comment.commentID).child(Firebase.auth.currentUser!!.uid).setValue(true)
                /*db.collection("likesComments").document(comment.commentID).collection("isLiked")
                    .document(Firebase.auth.currentUser!!.email.toString()).set(docLiked)
                db.collection("comments").document("comments")
                    .collection(comment.recipeID).document(comment.commentID)
                    .update("likes", FieldValue.increment(1))*/
            }else{
                reference.child("likesComments").child(comment.commentID).child(Firebase.auth.currentUser!!.uid).removeValue()
                /*db.collection("likesComments").document(comment.commentID).collection("isLiked")
                    .document(Firebase.auth.currentUser!!.email.toString()).delete()
                db.collection("comments").document("comments")
                    .collection(comment.recipeID).document(comment.commentID)
                    .update("likes", FieldValue.increment(-1))*/
            }
        }
    }

    private fun getAnswers(comment: Comment) {
        reference.child("comments").child(comment.recipeID).child(comment.commentID).child("answers").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount.toString() == "0"){
                    binding.vtpAnswerRC.visibility = View.GONE
                    binding.txvQuantAnswerRC.visibility = View.GONE
                }else{
                    binding.vtpAnswerRC.visibility = View.VISIBLE
                    ReadAnswers(comment.recipeID,comment.commentID,loadAdapterRCVAnswers())
                    binding.txvQuantAnswerRC.visibility = View.VISIBLE
                    binding.txvQuantAnswerRC.text = if(snapshot.childrenCount.toString() == "1") "Ver 1 respuesta" else "Ver ${snapshot.childrenCount} respuestas"
                }

                if(!comment.opened){
                    binding.txvQuantAnswerRC.text = if(snapshot.childrenCount.toString() == "1") "Ver 1 respuesta" else "Ver ${snapshot.childrenCount} respuestas"
                }else{
                    binding.txvQuantAnswerRC.text = if(snapshot.childrenCount.toString() == "1") "Ocultar 1 respuesta" else "Ocultar ${snapshot.childrenCount} respuestas"
                }

                binding.txvQuantAnswerRC.setOnClickListener {
                    if(!comment.opened){
                        binding.cstExpandComments.visibility= View.VISIBLE
                        binding.txvQuantAnswerRC.text = if(snapshot.childrenCount.toString() == "1") "Ocultar 1 respuesta" else "Ocultar ${snapshot.childrenCount} respuestas"
                    }else{
                        binding.cstExpandComments.visibility= View.GONE
                        binding.txvQuantAnswerRC.text = if(snapshot.childrenCount.toString() == "1") "Ver 1 respuesta" else "Ver ${snapshot.childrenCount} respuestas"
                    }
                    comment.opened = !comment.opened
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun ReadAnswers(recipeID: String, commentID: String, adapter: AnswerAdapter) {
        reference.child("comments").child(recipeID).child(commentID).child("answers").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                answerList.clear()
                snapshot.children.forEach{ doc ->
                    answerList.add(doc.getValue(Comment::class.java)!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        /*db.collection("comments").document("comments").collection(recipeID).document(commentID).collection("answers").addSnapshotListener { value, error ->
            if (error!=null){
                Log.w("TAG", "Listen Failed")
                return@addSnapshotListener
            }else{
                if(value!=null){
                    answerList.clear()
                    value.documents.forEach{ doc ->
                        answerList.add(doc.toObject()!!)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }*/
    }

    private fun loadAdapterRCVAnswers(): AnswerAdapter {
        binding.rcvAnswerCommentsRC.setHasFixedSize(true)
        binding.rcvAnswerCommentsRC.layoutManager = LinearLayoutManager(binding.rcvAnswerCommentsRC.context)
        val rcvCommentsAdapter = AnswerAdapter()
        rcvCommentsAdapter.submitList(answerList)
        binding.rcvAnswerCommentsRC.adapter = rcvCommentsAdapter
        return rcvCommentsAdapter
    }

    private fun isLiked(commentID: String) {
        reference.child("likesComments").child(commentID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(Firebase.auth.currentUser!!.uid).exists()) {
                    binding.imvLikeRC.setImageResource(R.drawable.ic_unlike_comment)
                    binding.imvLikeRC.tag = "liked"
                }
                else{
                    binding.imvLikeRC.setImageResource(R.drawable.ic_like_comment)
                    binding.imvLikeRC.tag = "like"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        /*val docRef = db.collection("likesComments").document(commentID).collection("isLiked")
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
        }*/
    }

    private fun loadComment(publisher:String, comment: String) {
        reference.child("users").child(publisher).get().addOnSuccessListener {
            val userName = it.child("userName").value.toString()
            val txtComment = "$userName $comment"
            val ss = SpannableString(txtComment)
            val manjariBold = Typeface.createFromAsset(binding.txvCommentRC.context.applicationContext.assets, "font/manjaribold.ttf")
            ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, userName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.txvCommentRC.text = ss
        }
        /*db.collection("users").document(publisher).get().addOnSuccessListener { doc ->
            val userName = doc["userName"].toString()
            val txtComment = "$userName $comment"
            val ss = SpannableString(txtComment)
            val manjariBold = Typeface.createFromAsset(binding.txvCommentRC.context.applicationContext.assets, "font/manjaribold.ttf")
            ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, userName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.txvCommentRC.text = ss
        }*/
    }

    private fun getLikes(commentID: String) {
        reference.child("likesComments").child(commentID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount.toString() == "0"){
                    binding.txvLikesRC.visibility = View.GONE
                }else{
                    binding.txvLikesRC.visibility = View.VISIBLE
                    binding.txvLikesRC.text = "${snapshot.childrenCount} Me gusta"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

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
        val diffDays = diffTime.toDays()

        if (diffSeconds>=60){
            if(diffMinutes>=60){
                if(diffHours>=24){
                    if(diffDays>7){
                        val diffWeeks = diffTime.toDays().toFloat()/7
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