package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityCheckMailBinding

class CheckMailActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCheckMailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckMailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val user = auth.currentUser

        binding.btnContinueVM.setOnClickListener {
            val profileUpdates = userProfileChangeRequest {  }
            user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    if(user.isEmailVerified){
                        reload()
                    }else{
                        Toast.makeText(this,"Por favor, verifique su correo.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.imbBackVM.setOnClickListener {
            logOut()
        }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                reload()
            }
            else{
                sendEmailVerification()
            }
        }
    }
    private fun sendEmailVerification(){
        val user = auth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Te hemos enviado un correo de verificación.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun reload()
    {
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
    }
    private fun logOut()
    {
        Firebase.auth.signOut()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}