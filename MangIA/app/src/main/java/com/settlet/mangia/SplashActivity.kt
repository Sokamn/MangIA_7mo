package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val reference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        auth = Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser
            if(currentUser != null){
                if(currentUser.isEmailVerified){
                    reference.child("users").child(currentUser.uid).get().addOnSuccessListener {
                        if (it.exists()){
                            updateUI(currentUser)
                        }else{
                            loadRegister(currentUser, "Email")
                        }
                    }
                } else{
                    val intent = Intent(this, CheckMailActivity::class.java)
                    startActivity(intent)
                }
            }else{
                startActivity(Intent(this,StartActivity::class.java))
                finish()
            }
        }, 3000)

    }


    private fun loadRegister(user: FirebaseUser?, loginMethod:String) {
        if(user!=null)
        {
            val intentReg = Intent(baseContext, RegisterActivity::class.java)
            intentReg.putExtra("pNumber",user.phoneNumber.toString())
            intentReg.putExtra("nName",user.displayName.toString())
            intentReg.putExtra("email",user.email.toString())
            intentReg.putExtra("photoProfile",user.photoUrl.toString())
            when(loginMethod){
                "Facebook"->intentReg.putExtra("logIn","Facebook")
                "Google"->intentReg.putExtra("logIn","Google")
                "Twitter"->intentReg.putExtra("logIn","Twitter")
                else->{
                    intentReg.putExtra("logIn","Mail")
                }
            }
            startActivity(intentReg)
            finish()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user!=null)
        {
            val intent = Intent(baseContext,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}