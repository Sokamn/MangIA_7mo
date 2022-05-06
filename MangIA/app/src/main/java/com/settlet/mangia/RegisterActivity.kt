package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityLoginBinding
import com.settlet.mangia.databinding.ActivityRegisterBinding
import java.util.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val db = Firebase.firestore
        var contador: Int = 0
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txpDateBirthR.setOnClickListener { showDatePickerDialog() }
        binding.btnContinueR.setOnClickListener{ // Boton de continuar inicial
            if(checkValue(contador)) {
                contador++
            }
        }
        binding.imbBackR.setOnClickListener{ // Boton de volver
            checkBack(contador)
            contador--
        }
        binding.txvPLoginR.setOnClickListener { // Volver al Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnContinueR2.setOnClickListener{ // Boton de finalizar registro
            val mailr = binding.txpMailR.text.toString()
            val passr = binding.txpPassR.text.toString()
            createAccount(mailr,passr)
            db.collection("users").document(mailr).set{
                hashMapOf("phoneNumber" to binding.txpTelR.text.toString(),
                    "userName" to binding.txpUserNameR.text.toString(),
                    "nickName" to binding.txpNNameR.text.toString(),
                    "country" to binding.txpCountryR.text.toString(),
                    "region" to binding.txpRegionR.text.toString(),
                    "dateBirth" to binding.txpDateBirthR.text.toString(),
                    "password" to binding.txpPassR.text.toString(),
                    "cantReports" to 0,
                    "edad" to Calendar.getInstance().get(Calendar.YEAR)-binding.txpDateBirthR.text.substring(binding.txpDateBirthR.text.length-4).trim().toInt(),
                    "cantFollows" to 0,
                    "cantFollowers" to 0
                )
            }
        }
        binding.btnCancelR.setOnClickListener { // Boton para cancelar registro ( NO ACEPTA TERMINOS DE CONDICIONES )
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
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
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }
    fun onDateSelected(day: Int, month: Int, year: Int){
        binding.txpDateBirthR.setText("$day/$month/$year")
    }


            private fun createAccount(email: String, password: String)
    {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, CheckMailActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Error al registrarse.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun checkValue(contador:Int):Boolean{
        val passwordRegex = Pattern.compile("^" + "(?=.*[-@#\$%^&+=])" + ".{6,}" + "$")
        when(contador){
            0 -> {
                if(binding.txpMailR.text.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(binding.txpMailR.text.toString()).matches())
                {
                    Toast.makeText(this,"Ingrese un mail valido.",Toast.LENGTH_SHORT).show()
                    return false
                }
                if(binding.txpTelR.text.isNotEmpty()&&binding.txpUserNameR.text.isNotEmpty()&&binding.txpNNameR.text.isNotEmpty()){
                    verifyUserNames()
                    register2()
                    return true
                }
                else{
                    showErrorEmpty()
                    return false
                }
            }
            1->{
                if(binding.txpCountryR.text.isNotEmpty()&&binding.txpRegionR.text.isNotEmpty()&&binding.txpDateBirthR.text.isNotEmpty()){
                    register3()
                    return true
                }
                else{
                    showErrorEmpty()
                    return false
                }
            }
            2->{
                if(binding.txpPassR.text.isEmpty()||binding.txpRepeatPassR.text.isEmpty())
                {
                    showErrorEmpty()
                    return false
                } else if(binding.txpPassR.text.isEmpty()||!passwordRegex.matcher(binding.txpPassR.text.toString()).matches()){
                    Toast.makeText(this,"La contraseña es debil.",Toast.LENGTH_SHORT).show()
                    return false
                } else if (binding.txpPassR.text.toString()!=binding.txpRepeatPassR.text.toString()){
                    Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show()
                    return false
                }  else{
                    register4()
                    return true
                }
            }
            3->{
                if(binding.txpMailR.text.isNotEmpty()&&binding.txpTelR.text.isNotEmpty()&&binding.txpUserNameR.text.isNotEmpty()&&binding.txpNNameR.text.isNotEmpty()){
                    register4()
                    return true
                }
                else{
                    showErrorEmpty()
                    return false
                }
            }
            else->{
                return false
            }
        }
    }
    private fun checkBack(contador: Int){//si contador es 0 se vuelve al intent de Login, si es 1 vuelve al registro 1
        when(contador)
        {
            0 ->{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            1 ->{register1()}
            2 ->{register2()}
            3 ->{register3()}
            else->{Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show()}
        }
    }
    private fun showErrorEmpty(){
        Toast.makeText(this,"Complete todos los campos",Toast.LENGTH_SHORT).show()
    }
    private fun verifyUserNames() {

    }
    private fun register1(){
        //register1
        binding.txvPLoginR.visibility = View.VISIBLE
        binding.imvNavBarNull.visibility = View.VISIBLE
        binding.txpMailR.visibility = View.VISIBLE
        binding.txpTelR.visibility = View.VISIBLE
        binding.txpUserNameR.visibility = View.VISIBLE
        binding.txpNNameR.visibility = View.VISIBLE
        //register2
        binding.txpDateBirthR.visibility = View.INVISIBLE
        binding.txpRegionR.visibility = View.INVISIBLE
        binding.txpCountryR.visibility = View.INVISIBLE
        binding.imvNavBar1.visibility = View.INVISIBLE
        //register3
        binding.txpPassR.visibility = View.INVISIBLE
        binding.txpRepeatPassR.visibility = View.INVISIBLE
        binding.txvRememberPPass.visibility = View.INVISIBLE
        binding.ckbRememberPass.visibility = View.INVISIBLE
        binding.imvNavBar2.visibility = View.INVISIBLE
        //register4
        binding.btnContinueR.visibility = View.VISIBLE
        binding.btnContinueR2.visibility = View.INVISIBLE
        binding.btnCancelR.visibility = View.INVISIBLE
        binding.txvTerminos.visibility = View.INVISIBLE
        binding.imvNavBar3.visibility = View.INVISIBLE
    }
    private fun register2(){
        //register1
        binding.imvNavBarNull.visibility = View.INVISIBLE
        binding.txpMailR.visibility = View.INVISIBLE
        binding.txpTelR.visibility = View.INVISIBLE
        binding.txpUserNameR.visibility = View.INVISIBLE
        binding.txpNNameR.visibility = View.INVISIBLE
        //register2
        binding.txvPLoginR.visibility = View.VISIBLE
        binding.txpDateBirthR.visibility = View.VISIBLE
        binding.txpRegionR.visibility = View.VISIBLE
        binding.txpCountryR.visibility = View.VISIBLE
        binding.imvNavBar1.visibility = View.VISIBLE
        //register3
        binding.txpPassR.visibility = View.INVISIBLE
        binding.txpRepeatPassR.visibility = View.INVISIBLE
        binding.txvRememberPPass.visibility = View.INVISIBLE
        binding.ckbRememberPass.visibility = View.INVISIBLE
        binding.imvNavBar2.visibility = View.INVISIBLE
        //register4
        binding.btnContinueR.visibility = View.VISIBLE
        binding.btnContinueR2.visibility = View.INVISIBLE
        binding.btnCancelR.visibility = View.INVISIBLE
        binding.txvTerminos.visibility = View.INVISIBLE
        binding.imvNavBar3.visibility = View.INVISIBLE
    }
    private fun register3(){
        //register1
        binding.imvNavBarNull.visibility = View.INVISIBLE
        binding.txpMailR.visibility = View.INVISIBLE
        binding.txpTelR.visibility = View.INVISIBLE
        binding.txpUserNameR.visibility = View.INVISIBLE
        binding.txpNNameR.visibility = View.INVISIBLE
        //register2
        binding.txpDateBirthR.visibility = View.INVISIBLE
        binding.txpRegionR.visibility = View.INVISIBLE
        binding.txpCountryR.visibility = View.INVISIBLE
        binding.imvNavBar1.visibility = View.INVISIBLE
        //register3
        binding.txpPassR.visibility = View.VISIBLE
        binding.txpRepeatPassR.visibility = View.VISIBLE
        binding.txvRememberPPass.visibility = View.VISIBLE
        binding.ckbRememberPass.visibility = View.VISIBLE
        binding.imvNavBar2.visibility = View.VISIBLE
        binding.txvPLoginR.visibility = View.VISIBLE
        //register4
        binding.btnContinueR.visibility = View.VISIBLE
        binding.btnContinueR2.visibility = View.INVISIBLE
        binding.btnCancelR.visibility = View.INVISIBLE
        binding.txvTerminos.visibility = View.INVISIBLE
        binding.imvNavBar3.visibility = View.INVISIBLE
    }
    private fun register4(){
        //register1
        binding.imvNavBarNull.visibility = View.INVISIBLE
        binding.txpMailR.visibility = View.INVISIBLE
        binding.txpTelR.visibility = View.INVISIBLE
        binding.txpUserNameR.visibility = View.INVISIBLE
        binding.txpNNameR.visibility = View.INVISIBLE
        //register2
        binding.txpDateBirthR.visibility = View.INVISIBLE
        binding.txpRegionR.visibility = View.INVISIBLE
        binding.txpCountryR.visibility = View.INVISIBLE
        binding.imvNavBar1.visibility = View.INVISIBLE
        //register3
        binding.txpPassR.visibility = View.INVISIBLE
        binding.txpRepeatPassR.visibility = View.INVISIBLE
        binding.txvRememberPPass.visibility = View.INVISIBLE
        binding.ckbRememberPass.visibility = View.INVISIBLE
        binding.imvNavBar2.visibility = View.INVISIBLE
        //register4
        binding.btnContinueR.visibility = View.INVISIBLE
        binding.txvPLoginR.visibility = View.INVISIBLE
        binding.btnContinueR2.visibility = View.VISIBLE
        binding.btnCancelR.visibility = View.VISIBLE
        binding.txvTerminos.visibility = View.VISIBLE
        binding.imvNavBar3.visibility = View.VISIBLE
    }
    private fun reload()
    {
        val intent = Intent(this,MainActivity::class.java)
        this.startActivity(intent)
    }
}