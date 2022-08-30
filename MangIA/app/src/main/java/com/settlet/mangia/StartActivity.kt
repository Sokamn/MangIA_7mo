package com.settlet.mangia

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityStartBinding


class StartActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityStartBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val reference = FirebaseDatabase.getInstance().reference
    private val RC_SIGN_IN = 45
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_ids))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        val wordtoSpan: Spannable = SpannableString(binding.txvBeginChefS.text)

        wordtoSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.secundaryColor)),
            19,
            binding.txvBeginChefS.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txvBeginChefS.text = wordtoSpan

        binding.imbFacebookS.setOnClickListener {

        }

        binding.imbGoogleS.setOnClickListener {
            logInG()
        }

        binding.btnLoginS.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.txvBeginChefS.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("logIn","mail")
            startActivity(intent)
        }
    }

    private fun logInG() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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
                    Toast.makeText(this,"Ocurrió un error inesperado. Por favor, intentelo más tarde...",
                        Toast.LENGTH_SHORT).show()
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
    private fun reload()
    {
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        finish()
    }
}