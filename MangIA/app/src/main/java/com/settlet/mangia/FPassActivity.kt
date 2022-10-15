package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityFpassBinding

class FPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFpassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFpassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        binding.imbBackFP.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.btnSendFP.setOnClickListener {
            val emailAddress = binding.txpMailFP.text.toString()
            Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    val intent = Intent(this, LoginActivity::class.java)
                    this.startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this,"Ingrese un email de una cuenta valida.",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}