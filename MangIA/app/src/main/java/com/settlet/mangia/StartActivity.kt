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
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityStartBinding


class StartActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityStartBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val reference = FirebaseDatabase.getInstance().reference
    private val callbackManager = CallbackManager.Factory.create()
    private val RC_SIGN_IN = 45
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FacebookSdk.sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(this);
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

        binding.imbTwitter.setOnClickListener {
            logInT()
        }
        binding.imbFacebookS.setOnClickListener {
            logInF()
        }

        binding.imbGoogleS.setOnClickListener {
            logInG()
        }

        binding.btnLoginS.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.txvBeginChefS.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("logIn","Email")
            startActivity(intent)
        }
    }

    private fun logInT() {
        val provider = OAuthProvider.newBuilder("twitter.com")
        provider.addCustomParameter("lang", "es")

        val pendingResultTask: Task<AuthResult>? = auth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener(
                    OnSuccessListener {
                        val user = auth.currentUser
                        if (user != null) {
                            reference.child("users").child(user.uid).get().addOnSuccessListener {
                                if (it.exists()){
                                    updateUI(user)
                                }else{
                                    loadRegister(user, "Twitter")
                                }
                            }
                        }
                    })
                .addOnFailureListener{
                        showError()
                    }
        } else {
            auth
                .startActivityForSignInWithProvider( /* activity= */this, provider.build())
                .addOnSuccessListener{
                        val user = auth.currentUser
                        if (user != null) {
                            reference.child("users").child(user.uid).get().addOnSuccessListener {
                                if (it.exists()){
                                    updateUI(user)
                                }else{
                                    loadRegister(user, "Twitter")
                                }
                            }
                        }
                    }
                .addOnFailureListener{
                        showError()
                    }
        }
    }

    private fun logInF() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email","public_profile"))

        LoginManager.getInstance().registerCallback(callbackManager, object:FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                if (result != null) {
                    handleFacebookAccessToken(result.accessToken)
                }
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
                showError()
            }

        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener { task->
            if(task.isSuccessful){
                val user = auth.currentUser
                if (user != null) {
                    reference.child("users").child(user.uid).get().addOnSuccessListener {
                        if (it.exists()){
                            updateUI(user)
                        }else{
                            loadRegister(user, "Facebook")
                        }
                    }
                }
            }else{
                Toast.makeText(this,"No puedes ingresar con esta cuenta",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showError() {
        //tirar error
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
                                loadRegister(user, "Google")
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

    public override fun onStart() {
        super.onStart()
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
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode,data)
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