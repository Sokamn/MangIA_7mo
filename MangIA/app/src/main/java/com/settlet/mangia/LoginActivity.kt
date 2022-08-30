package com.settlet.mangia

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Model.User
import com.settlet.mangia.databinding.ActivityLoginBinding
import java.util.*
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    private val reference = FirebaseDatabase.getInstance().reference
    private val RC_SIGN_IN = 45
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val wordtoSpan: Spannable = SpannableString(binding.txvForgotPassL.text)

        wordtoSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.secundaryColor)),
            26,
            binding.txvForgotPassL.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txvForgotPassL.text = wordtoSpan

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_ids))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        binding.imvPassVisible.setOnTouchListener { v, event ->
            val action = event.action
            when(action){
                MotionEvent.ACTION_DOWN -> {
                    binding.txpPassL.inputType = 145
                    binding.imvPassVisible.setImageResource(R.drawable.ic_slashpass_btn)
                    val typeface: Typeface? = ResourcesCompat.getFont(this, R.font.manjarithin);
                    binding.txpPassL.typeface = typeface

                }
                MotionEvent.ACTION_UP -> {
                    binding.txpPassL.inputType = 129
                    binding.imvPassVisible.setImageResource(R.drawable.ic_viewpass_btn)
                    val typeface: Typeface? = ResourcesCompat.getFont(this, R.font.manjarithin);
                    binding.txpPassL.typeface = typeface
                }
            }
            true
        }

        binding.btnContinueL.setOnClickListener {
            val maill = binding.txpMailL.text.toString()
            val passl = binding.txpPassL.text.toString()
            if(binding.txpMailL.text.isEmpty()||binding.txpPassL.text.isEmpty()){
                showErrorEmpty()
            } else{
                logInM(maill,passl)
            }
        }

        binding.txvForgotPassL.setOnClickListener {
            val intent = Intent(this, FPassActivity::class.java)
            startActivity(intent)
        }
        binding.imbGoogleL.setOnClickListener {
            logInG()
        }
        binding.imbFacebookL.setOnClickListener {
            Toast.makeText(this,"Facebook",Toast.LENGTH_SHORT).show()
        }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                reload()
            } else{
                val intent = Intent(this, CheckMailActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    if (user != null) {
                        reference.child("users").child(user.uid).get().addOnSuccessListener {
                            if (it.exists()){
                                updateUI(user)
                            }else{
                                loadRegister(user)
                            }
                        }
                    }

                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this,"Ocurrió un error inesperado. Por favor, intentelo más tarde...",Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun loadRegister(user: FirebaseUser?) {
        if(user!=null)
        {
            val intentReg = Intent(baseContext, RegisterActivity::class.java)
            intentReg.putExtra("pNumber",user.phoneNumber.toString())
            intentReg.putExtra("nName",user.displayName.toString())
            intentReg.putExtra("email",user.email.toString())
            intentReg.putExtra("logIn","Google")
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

    private fun logInG() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun logInM(email : String, password: String){ // Login Mail Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    val currentUser = auth.currentUser
                    if(currentUser != null){
                        if(currentUser.isEmailVerified){
                            reload()
                        }
                        else{
                            val intent = Intent(this,CheckMailActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    finish()
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun reload()
    {
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        finish()
    }
    private fun showErrorEmpty(){
        Toast.makeText(this,"Complete todos los campos",Toast.LENGTH_SHORT).show()
    }
}


