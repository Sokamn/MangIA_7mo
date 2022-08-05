package com.settlet.mangia.ViewHolder

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Adapter.SliderAdapter
import com.settlet.mangia.CommentsActivity
import com.settlet.mangia.Model.CustomTypefaceSpan
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.ProfileActivity
import com.settlet.mangia.R
import com.settlet.mangia.UserRateActivity
import com.settlet.mangia.databinding.RecipeItemBinding
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

class PreviewRecipeViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding = RecipeItemBinding.bind(view)
    private val db = Firebase.firestore
    private val storageReference = FirebaseStorage.getInstance().reference
    private val listImages = mutableListOf<String>()

    fun render(recipe:Recipe){
        if (recipe.listImages.size == 1){
            val fileRef = storageReference.child(recipe.listImages.first())
            fileRef.downloadUrl.addOnSuccessListener { result ->
                Glide.with(binding.imvUniquePost.context)
                    .load(result)
                    .into(binding.imvUniquePost)
            }
            binding.crdvwSliderRI.visibility = View.GONE
        }else{
            recipe.listImages.forEach {
                val fileRef = storageReference.child(it)
                fileRef.downloadUrl.addOnSuccessListener { result ->
                    listImages.add(result.toString())
                    if (listImages.size == recipe.listImages.size){
                        Log.d("IMAGE",listImages.toString())
                        binding.imgsldrCarruselRI.setSliderAdapter(SliderAdapter(listImages,false))
                    }
                }.addOnFailureListener {
                    Log.d("IMAGE","No se ha podido cargar la imagen")
                }
            }
            binding.imvUniquePost.visibility = View.INVISIBLE
            binding.imvUniquePost.setImageResource(R.drawable.profile_picture)
            binding.imgsldrCarruselRI.visibility = View.VISIBLE
        }
        val pImageRef = storageReference.child("users/${recipe.publisher}/profile.jpg")
        pImageRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(binding.imvProfilePictureRI.context)
                .load(result)
                .into(binding.imvProfilePictureRI)
        }
        val txtDescription = recipe.title + " " + recipe.description
        if(txtDescription.length > 70){
            addReadMore(txtDescription,binding.txvDescription,recipe.title.length)
        }
        else{
            val ss = SpannableString(txtDescription)
            val manjariBold = Typeface.createFromAsset(binding.txvDescription.context.applicationContext.assets, "font/manjaribold.ttf")
            ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, recipe.title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.txvDescription.text = ss
        }
        if(recipe.numberTimesValored==0){
            binding.txvValoration.visibility = View.GONE
        }else{
            binding.txvValoration.visibility = View.VISIBLE
        }
        binding.txvValoration.text = recipe.numberTimesValored.toString() + " valoraciones"
        val docRef = db.collection("users").document(recipe.publisher)
        docRef.addSnapshotListener { value, error ->
            if(error!=null){
                Log.w("TAG","Listen Failed")
                return@addSnapshotListener
            }
            if(value!=null && value.exists()) {
                val user = value.toObject<com.settlet.mangia.Model.User>()
                if (user != null) {
                    binding.txvUserNameRI.text = user.userName
                    binding.txvUserCountryRI.text = user.country
                }
            }
        }
        val timeLaunch = LocalDateTime.parse(recipe.timeLaunch, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val timeNow = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val diffTime = Duration.between(timeLaunch,timeNow)
        val diffMinutes = diffTime.toMinutes()
        val diffHours = diffTime.toHours()
        val diffDays = diffTime.toDays()
        val diffYears = ChronoUnit.YEARS.between(timeLaunch,timeNow)
        if(diffMinutes>=60){
            if(diffHours>=24){
                if(diffDays>7){
                    if (diffYears>=1){
                        when(timeLaunch.monthValue){
                            1->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de enero de ${timeLaunch.year}"
                            2->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de febrero ${timeLaunch.year}"
                            3->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de marzo ${timeLaunch.year}"
                            4->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de abril ${timeLaunch.year}"
                            5->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de mayo ${timeLaunch.year}"
                            6->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de junio ${timeLaunch.year}"
                            7->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de julio ${timeLaunch.year}"
                            8->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de agosto ${timeLaunch.year}"
                            9->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de septiembre ${timeLaunch.year}"
                            10->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de octubre ${timeLaunch.year}"
                            11->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de noviembre ${timeLaunch.year}"
                            12->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de diciembre ${timeLaunch.year}"
                            else-> binding.txvLaunchTime.text = ""
                        }
                    }else{
                        when(timeLaunch.monthValue){
                            1->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de enero"
                            2->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de febrero"
                            3->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de marzo"
                            4->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de abril"
                            5->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de mayo"
                            6->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de junio"
                            7->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de julio"
                            8->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de agosto"
                            9->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de septiembre"
                            10->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de octubre"
                            11->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de noviembre"
                            12->binding.txvLaunchTime.text = "${timeLaunch.dayOfMonth} de diciembre"
                            else-> binding.txvLaunchTime.text = ""
                        }
                    }
                }else{
                    if (diffDays.toInt() == 1){
                        binding.txvLaunchTime.text = "Hace 1 día"
                    }else{
                        binding.txvLaunchTime.text = "Hace $diffDays días"
                    }
                }
            } else{
                if (diffHours.toInt() == 1){
                    binding.txvLaunchTime.text = "Hace 1 hora"
                } else{
                    binding.txvLaunchTime.text = "Hace $diffHours horas"
                }
            }
        } else{
            if (diffMinutes.toInt() <= 1){
                binding.txvLaunchTime.text = "Hace 1 minuto"
            } else{
                binding.txvLaunchTime.text = "Hace $diffMinutes minutos"
            }
        }
        when(recipe.cantComments){
            0-> binding.txvComments.visibility = View.GONE
            1-> binding.txvComments.text = "Ver 1 comentario"
            else -> binding.txvComments.text = "Ver los ${recipe.cantComments} comentarios"
        }
        binding.txvComments.text = if (recipe.cantComments == 1) "Ver 1 comentario" else "Ver los ${recipe.cantComments} comentarios"
        binding.txvValoration.text = if (recipe.numberTimesValored == 1) "1 valoración" else "${recipe.numberTimesValored} valoraciones"
        isLiked(recipe)
        isSaved(recipe)


        binding.cstTopBar.setOnClickListener { // Mandar al perfil del usuario
            val editor = itemView.context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileEmail", recipe.publisher)
            editor.apply()
            itemView.context.startActivity(Intent(itemView.context, ProfileActivity::class.java))
        }
        binding.cstPost.setOnClickListener { // Mandar a la receta completa

        }
        binding.imvStar1.setOnClickListener { // Dar una estrella
            IsLikedBTN(recipe,1)
        }
        binding.imvStar2.setOnClickListener { // Dar dos estrella
            IsLikedBTN(recipe,2)
        }
        binding.imvStar3.setOnClickListener { // Dar tres estrella
            IsLikedBTN(recipe,3)
        }
        binding.imvStar4.setOnClickListener { // Dar cuatro estrella
            IsLikedBTN(recipe,4)
        }
        binding.imvStar5.setOnClickListener { // Dar cinco estrella
            IsLikedBTN(recipe,5)
        }
        binding.imvSave.setOnClickListener { // Guardar en mis recetas favoritas
            val docSaved = hashMapOf<String, Any>()
            if(binding.imvSave.tag.equals("save")){
                docSaved["isSaved"] = true.toString()
                db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).set(docSaved)
            }else{
                db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).delete()
            }

        }
        binding.imvComment.setOnClickListener { // Mandar a comentar
            val intent = Intent(binding.imvStar1.context, CommentsActivity::class.java )
            intent.putExtra("recipeID",recipe.recipeID)
            binding.imvStar1.context.startActivity(intent)
        }
        binding.imvOptions.setOnClickListener { // Lanzar popup con las posibilidades de la receta

        }
        binding.imvShare.setOnClickListener { // Compartir la receta
            Toast.makeText(binding.imvShare.context,"Compartir recetas aún no está implementado.",Toast.LENGTH_SHORT).show()
        }
        binding.txvValoration.setOnClickListener {  // Mostrar todos los usuarios que valoraron la receta, y cual fue su valoración.
            val intent = Intent(binding.imvStar1.context, UserRateActivity::class.java )
            intent.putExtra("recipeID",recipe.recipeID)
            binding.imvStar1.context.startActivity(intent)
        }
    }

    private fun isSaved(recipe: Recipe){
        db.collection("saves").document(Firebase.auth.currentUser!!.email.toString()).collection("isSaved").document(recipe.recipeID).addSnapshotListener { value, error ->
            if (error!=null){
                Log.w("TAG", "Listen Failed")
                return@addSnapshotListener
            }
            else{
                if (value!=null){
                    if (value.exists()){
                        binding.imvSave.setImageResource(R.drawable.ic_close)
                        binding.imvSave.tag = "saved"
                    }else{
                        binding.imvSave.setImageResource(R.drawable.ic_check_recipe)
                        binding.imvSave.tag = "save"
                    }
                }
            }
        }
    }

    private fun isLiked(recipe: Recipe) {
        val docRef = db.collection("likes").document(recipe.recipeID).collection("isLiked")
            .document(Firebase.auth.currentUser!!.email.toString())
        docRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("TAG", "Listen Failed")
                return@addSnapshotListener
            }
            if (value != null) {
                if (value.exists()) {
                    val rateUser = value["rate"]
                    updateMyRate(rateUser.toString().toFloat().roundToInt())
                }
                else{
                    binding.imvStar1.setImageResource(R.drawable.ic_add)
                    binding.imvStar2.setImageResource(R.drawable.ic_add)
                    binding.imvStar3.setImageResource(R.drawable.ic_add)
                    binding.imvStar4.setImageResource(R.drawable.ic_add)
                    binding.imvStar5.setImageResource(R.drawable.ic_add)
                }
            }
        }
    }

    private fun IsLikedBTN(recipe: Recipe,rate:Int) {
        val docFollows = hashMapOf<String, Any>()
        docFollows["rate"] = rate
        val docRef = db.collection("likes").document(recipe.recipeID).collection("isLiked").document(Firebase.auth.currentUser!!.email.toString())
        docRef.get().addOnSuccessListener { doc ->
            if(doc!=null) {
                if(doc.exists()){
                    if(rate == doc["rate"].toString().toFloat().roundToInt()){
                        binding.imvStar1.setImageResource(R.drawable.ic_add)
                        binding.imvStar2.setImageResource(R.drawable.ic_add)
                        binding.imvStar3.setImageResource(R.drawable.ic_add)
                        binding.imvStar4.setImageResource(R.drawable.ic_add)
                        binding.imvStar5.setImageResource(R.drawable.ic_add)
                        db.collection("recipes").document(recipe.recipeID).update("numberTimesValored", FieldValue.increment(-1))
                        docRef.delete()
                    }
                    else{
                        updateMyRate(rate)
                        docRef.update("rate", rate)
                    }
                }
                else{
                    updateMyRate(rate)
                    db.collection("recipes").document(recipe.recipeID).update("numberTimesValored", FieldValue.increment(1))
                    docRef.set(docFollows)
                }
                updateRecipeRate(recipe)
            }
        }
    }

    private fun updateRecipeRate(recipe: Recipe) {
        db.collection("likes").document(recipe.recipeID).collection("isLiked").get().addOnSuccessListener(){ documents ->
            var ratePlus = 0
            documents.forEach { doc ->
                ratePlus+=doc["rate"].toString().toInt()
            }
            if (recipe.numberTimesValored==0){
                db.collection("recipes").document(recipe.recipeID).update("stars", 0)
            }else{
                val promRecipeRate = ratePlus/recipe.numberTimesValored
                db.collection("recipes").document(recipe.recipeID).update("stars", promRecipeRate)
            }
        }
    }

    private fun updateMyRate(rate: Int) {
        when(rate){
            1-> {
                binding.imvStar1.setImageResource(R.drawable.ic_remove)
                binding.imvStar2.setImageResource(R.drawable.ic_add)
                binding.imvStar3.setImageResource(R.drawable.ic_add)
                binding.imvStar4.setImageResource(R.drawable.ic_add)
                binding.imvStar5.setImageResource(R.drawable.ic_add)
            }
            2-> {
                binding.imvStar1.setImageResource(R.drawable.ic_remove)
                binding.imvStar2.setImageResource(R.drawable.ic_remove)
                binding.imvStar3.setImageResource(R.drawable.ic_add)
                binding.imvStar4.setImageResource(R.drawable.ic_add)
                binding.imvStar5.setImageResource(R.drawable.ic_add)
            }
            3->{
                binding.imvStar1.setImageResource(R.drawable.ic_remove)
                binding.imvStar2.setImageResource(R.drawable.ic_remove)
                binding.imvStar3.setImageResource(R.drawable.ic_remove)
                binding.imvStar4.setImageResource(R.drawable.ic_add)
                binding.imvStar5.setImageResource(R.drawable.ic_add)
            }
            4-> {
                binding.imvStar1.setImageResource(R.drawable.ic_remove)
                binding.imvStar2.setImageResource(R.drawable.ic_remove)
                binding.imvStar3.setImageResource(R.drawable.ic_remove)
                binding.imvStar4.setImageResource(R.drawable.ic_remove)
                binding.imvStar5.setImageResource(R.drawable.ic_add)
            }
            5-> {
                binding.imvStar1.setImageResource(R.drawable.ic_remove)
                binding.imvStar2.setImageResource(R.drawable.ic_remove)
                binding.imvStar3.setImageResource(R.drawable.ic_remove)
                binding.imvStar4.setImageResource(R.drawable.ic_remove)
                binding.imvStar5.setImageResource(R.drawable.ic_remove)
            }
            else -> binding.imvStar1.setImageResource(R.drawable.ic_remove)
        }
    }


    private fun addReadMore(text: String, textView: TextView, titleCharacters: Int) {
        val manjariBold = Typeface.createFromAsset(binding.txvDescription.context.applicationContext.assets, "font/manjaribold.ttf")
        val manjariThin = Typeface.createFromAsset(binding.txvDescription.context.applicationContext.assets, "font/manjarithin.ttf")
        val lines = text.split("\r\n","\r","\n")
        var charsTitle = 0
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addReadLess(text, textView,titleCharacters)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = binding.txvDescription.context.resources.getColor(R.color.black)
            }
        }
        if(lines.size > 3){
            var count = 0
            text.forEach {
                count++
                if(it == '\n'){
                    if(charsTitle==0){
                        charsTitle = count
                    }
                }
            }
            val ss = SpannableString(text.substring(0, charsTitle-1) + "... Leer más")
            ss.setSpan(clickableSpan, ss.length - 12, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(CustomTypefaceSpan("",manjariThin), ss.length - 12, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, titleCharacters, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            textView.text = ss
        }
        else{
                val ss = SpannableString(text.substring(0, 70) + "... Leer más")
                ss.setSpan(clickableSpan, ss.length - 12, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                ss.setSpan(CustomTypefaceSpan("",manjariThin), ss.length - 12, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, titleCharacters, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                textView.text = ss
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun addReadLess(text: String, textView: TextView, titleCharacters:Int) {
        val manjariBold = Typeface.createFromAsset(binding.txvDescription.context.applicationContext.assets, "font/manjaribold.ttf")
        val manjariThin = Typeface.createFromAsset(binding.txvDescription.context.applicationContext.assets, "font/manjarithin.ttf")
        val ss = SpannableString("$text Leer menos")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addReadMore(text, textView,titleCharacters)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = binding.txvDescription.context.resources.getColor(R.color.black)
            }
        }
        ss.setSpan(clickableSpan, ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(CustomTypefaceSpan("",manjariThin), ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, titleCharacters, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}