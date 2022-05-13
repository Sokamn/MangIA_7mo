package com.settlet.mangia

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityLoginBinding
import java.util.concurrent.TimeUnit
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId:String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var stateVPhone = false
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.swcLoginPhone.setOnCheckedChangeListener{
                _, isChecked -> isChecked
            logPhone()
        }

        binding.btnContinueL.setOnClickListener {
            if(binding.swcLoginPhone.isChecked)
            {
                stateVPhone=logInP()
            }else{
                val maill = binding.txpMailL.text.toString()
                val passl = binding.txpPassL.text.toString()
                if(binding.txpMailL.text.isEmpty()||binding.txpPassL.text.isEmpty()){
                    showErrorEmpty()
                } else{
                    logInM(maill,passl)
                }
            }
        }
        if(stateVPhone){
            callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d("TAG", "onVerificationCompleted:$credential")
                    signInWithPhoneAuthCredential(credential)
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    Log.w("TAG", "onVerificationFailed", e)
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(baseContext, "Codigo de verificación erroneo.", Toast.LENGTH_SHORT).show()
                    } else if (e is FirebaseTooManyRequestsException) {
                        Toast.makeText(baseContext, "Has realizado demaciadas solicitudes. Intentelo más tarde.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d("TAG", "onCodeSent:$verificationId")
                    storedVerificationId = verificationId
                    resendToken = token
                    Toast.makeText(baseContext, "Se te ha enviado el codigo de verificación por SMS.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext,VerifyPhoneActivity::class.java)
                    intent.putExtra("storedVerificationId",storedVerificationId)
                    startActivity(intent)
                }
            }

        }

        binding.txvPRegisterL.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.txvForgotPassL.setOnClickListener {
            val intent = Intent(this, FPassActivity::class.java)
            startActivity(intent)
        }
        binding.imbGoogleL.setOnClickListener {
            Toast.makeText(this,"Google",Toast.LENGTH_SHORT).show()
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

    private fun logInM(email : String, password: String){ // Login Mail Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    reload()
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun logInP():Boolean
    {
        var number=binding.txpPhoneNumber.text.toString().trim()
        val nArea=binding.txpNArea.text.toString().trim()

        if(number.isNotEmpty()||nArea.isNotEmpty()){
            number=nArea+number
            sendVerificationCode(number)
            return true
        }else{
            Toast.makeText(this,"Ingrese todos los campos",Toast.LENGTH_SHORT).show()
            return false
        }
    }
    private fun sendVerificationCode(phoneNumber: String)
    {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun logPhone() // Switch: Cambia el inicio de Sesión de Email a Telefono
    {
        if(binding.swcLoginPhone.isChecked){
            //login phone
            binding.txpPassL.visibility = View.INVISIBLE
            binding.txpMailL.visibility = View.INVISIBLE
            binding.txvForgotPassL.visibility = View.INVISIBLE
            binding.txpNArea.visibility = View.VISIBLE
            binding.txpPhoneNumber.visibility = View.VISIBLE
        }else
        {
            //login email
            binding.txpPassL.visibility = View.VISIBLE
            binding.txpMailL.visibility = View.VISIBLE
            binding.txvForgotPassL.visibility = View.VISIBLE
            binding.txpNArea.visibility = View.INVISIBLE
            binding.txpPhoneNumber.visibility = View.INVISIBLE
        }
    }
    private fun reload()
    {
        val intent = Intent(this,MainActivity::class.java)
        this.startActivity(intent)
    }
    private fun showErrorEmpty(){
        Toast.makeText(this,"Complete todos los campos",Toast.LENGTH_SHORT).show()
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithCredential:success")
                    val user = task.result?.user
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this,"El codigo de verificación que ha ingresado es invalido",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}