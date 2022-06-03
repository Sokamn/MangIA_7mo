package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.Toast
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import java.util.regex.Pattern
import kotlin.math.log

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        World.init(applicationContext)
        val db = Firebase.firestore
        var contador: Int = 0
        val continents = resources.getStringArray(R.array.continents)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val arrayAdapterC = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, continents)
        val arrayAdapterLAfrica = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lAfrican)
        val arrayAdapterLAsia = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lAsia)
        val arrayAdapterLASouth = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lSAmerica)
        val arrayAdapterLANorth = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lNAmerica)
        val arrayAdapterLOceania = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lOceania)
        val arrayAdapterLEurope = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countryProvider.lEurope)

        binding.txpRegionR.setAdapter(arrayAdapterC)

        binding.txpRegionR.setOnClickListener {
            if(binding.txpCountryR.text.toString() == "")
            {
                binding.txpRegionR.isFocusableInTouchMode = true
                binding.txpCountryR.isFocusableInTouchMode = false
                binding.txpRegionR.requestFocus()
            }
            else
            {
                binding.txpCountryR.setText("")
                binding.txpRegionR.isFocusableInTouchMode = true
                binding.txpCountryR.isFocusableInTouchMode = false
                binding.txpRegionR.requestFocus()
            }
        }
        binding.txpCountryR.setOnClickListener {
            when(binding.txpRegionR.text.toString()){
            "Africa"->{
                Log.w("TAG", "${countryProvider.lAfrican}")
                binding.txpCountryR.setAdapter(arrayAdapterLAfrica)
                binding.txpCountryR.isFocusableInTouchMode = true
                binding.txpCountryR.requestFocus()
                binding.txpRegionR.isFocusableInTouchMode = false
            }
            "Asia"->{
                Log.w("TAG", "${countryProvider.lAsia}")
                binding.txpCountryR.setAdapter(arrayAdapterLAsia)
                binding.txpCountryR.isFocusableInTouchMode = true
                binding.txpCountryR.requestFocus()
                binding.txpRegionR.isFocusableInTouchMode = false
            }
            "America del Norte o Central"->{
                Log.w("TAG", "${countryProvider.lNAmerica}")
                binding.txpCountryR.setAdapter(arrayAdapterLANorth)
                binding.txpCountryR.isFocusableInTouchMode = true
                binding.txpCountryR.requestFocus()
                binding.txpRegionR.isFocusableInTouchMode = false
            }
            "America del Sur"->{
                Log.w("TAG", "${countryProvider.lSAmerica}")
                binding.txpCountryR.setAdapter(arrayAdapterLASouth)
                binding.txpCountryR.isFocusableInTouchMode = true
                binding.txpCountryR.requestFocus()
                binding.txpRegionR.isFocusableInTouchMode = false
            }
            "Europa"->{
                Log.w("TAG", "${countryProvider.lEurope}")
                binding.txpCountryR.setAdapter(arrayAdapterLEurope)
                binding.txpCountryR.isFocusableInTouchMode = true
                binding.txpCountryR.requestFocus()
                binding.txpRegionR.isFocusableInTouchMode = false
            }
            "Oceania"->{
                Log.w("TAG", "${countryProvider.lOceania}")
                binding.txpCountryR.setAdapter(arrayAdapterLOceania)
                binding.txpCountryR.isFocusableInTouchMode = true
                binding.txpCountryR.requestFocus()
                binding.txpRegionR.isFocusableInTouchMode = false
            }
            else ->{
                Log.w("TAG", "VACIO ABSOLUTO")
                binding.txpCountryR.setAdapter(null)
                Toast.makeText(this,"Primero ingrese su región / Continente",Toast.LENGTH_LONG).show()
                binding.txpRegionR.requestFocus()
            }
        }
        }

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
            val docUser = hashMapOf("age" to Calendar.getInstance().get(Calendar.YEAR)-binding.txpDateBirthR.text.substring(binding.txpDateBirthR.text.length-4).trim().toInt(),
                "biography" to "",
                "cantFollowers" to 0,
                "cantFollows" to 0,
                "cantRecipes" to 0,
                "cantReports" to 0,
                "country" to binding.txpCountryR.text.toString(),
                "dateBirth" to binding.txpDateBirthR.text.toString(),
                "dateCreationAccount" to Calendar.getInstance().time.toString(),
                "email" to binding.txpMailR.text.toString(),
                "nickName" to binding.txpNNameR.text.toString(),
                "password" to passr,
                "phoneNumber" to binding.txpTelR.text.toString(),
                "region" to binding.txpRegionR.text.toString(),
                "userName" to binding.txpUserNameR.text.toString()
            )
            db.collection("users").document(mailr).set(docUser)
        }
        binding.btnCancelR.setOnClickListener { // Boton para cancelar registro ( NO ACEPTA TERMINOS DE CONDICIONES )
            val intent = Intent(this,LoginActivity::class.java)
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
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }
    fun onDateSelected(day: Int, month: Int, year: Int){
        binding.txpDateBirthR.setText("$day/${month+1}/$year")
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
                    Toast.makeText(baseContext, "Error al registrarse. Por favor, intentelo más tarde.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun checkValue(contador:Int):Boolean{
        val passwordRegex = Pattern.compile("^" + "(?=.*[A-Z])" + "(?=.*[0-9])" + ".{6,}" + "$")
        val badWords = arrayListOf<String>("sorete","imbecil","tarado","pelotudo","pajero","pajera","pelotuda","tarada","puto","puta","concha","culo","poronga","verga","pito","pene" + "nigga" , "trola" , "trolo" , "caca" , "down" , "mierda" , "nazi" , "hitler" , "estupido" , "coger" , "cojer" , "pendejo " , "pendeja" , "porno" , "orto" , "sexo" , "pinche" , "pinchi" , "cojo" , "cabrón" , "cabrona" , "mames" , "pendejos" , "pendejas" , "chinga" , "mamadas" , "pendejadas" , "mama huevo" , "pete" , "wueon" , "xuxa" , "weon" , "weonado" , "weona" , "coño" , "aguevoniado" , "guevon" , "pajuo" , "marica", "monda" , "marrana" , "marrano" ,"monda" , "pijudo" , "hijueputa" , "cotopla" , "pichurria" , "picha" , "mother fucker" , "fuck" , "ass" , "orgy" , "bitch" , "suck" , "my balls" , "slut " , "whore" , "hoe" , "chupamela" , "culito" , "cojida" , "cojiendo" , "zoofilia" , "putito" , "reputo" , "free viagra" , "taradito", "taradita" , "pelotudito" , "pelotudita" , "pelotuditos", "pelotuditas" , "putita" , "poronguita" , "verguita" , "pitito" , "trolito" , "trolita" , "caquita" , "estupidito" , "estupidita" , "pendejito" , "pendejita" , "putitos" , "putitas" , "poronguitas" , "porongotas" , "porongota" , "porongon" , "verguitas", "vergotas" , "vergota" , "pititos" , "pitotes" , "pitote" , "trolitos" , "trolitas" , "caquitas" , "cacotas" , "estupiditos" , "estupiditas" , "pendejitos" , "pendejitas" , "feto" , "cigoto" , "caka" , "kaka" , "kk" , "joto" , "jota" , "kaco" , "kago" , "kojo" , "kulo" , "mamo" , "meaas" , "mion" , "mula" , "pedo" , "qulo" , "buey" , "caco" , "cago" , "cako" , "coja" , "coji" , "guey" , "kaca" , "kaga" , "koge" , "mame" , "mear" , "meon" , "moco")
        when(contador){
            0 -> {
                if(binding.txpMailR.text.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(binding.txpMailR.text.toString()).matches())
                {
                    Toast.makeText(this,"Ingrese un mail valido.",Toast.LENGTH_SHORT).show()
                    return false
                }
                if(binding.txpTelR.text.isNotEmpty()&&binding.txpUserNameR.text.isNotEmpty()&&binding.txpNNameR.text.isNotEmpty()){
                    if(verifyUserNames(badWords,binding.txpUserNameR.text.toString(),binding.txpNNameR.text.toString()))
                    {
                        //whereEqualTo(email, con email que se registra, si es verdadero y encuentra el documento, que tire un Toast diciendo "Email ya registrado, por favor, ingrese otro mail."
                        register2()
                        return true
                    }
                    else{
                        Toast.makeText(this, "Se detectó un nombre de usuario o apodo ofensivo.",Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
                else{
                    showErrorEmpty()
                    return false
                }
            }
            1->{
                if(binding.txpCountryR.text.isNotEmpty()&&binding.txpRegionR.text.isNotEmpty()&&binding.txpDateBirthR.text.isNotEmpty()){
                    val edad = Calendar.getInstance().get(Calendar.YEAR)-binding.txpDateBirthR.text.substring(binding.txpDateBirthR.text.length-4).trim().toInt()
                    if(edad < 13)
                    {
                        Toast.makeText(this, "Usted tiene menos de 13 años, por favor, ingrese una fecha valida.",Toast.LENGTH_SHORT).show()
                        return false
                    }else{
                        for (c in resources.getStringArray(R.array.continents))
                        {
                            if(binding.txpRegionR.text.toString() == c)
                            {
                                for (C in countryProvider.lTPaises)
                                {
                                    if(binding.txpCountryR.text.toString() == C)
                                    {
                                        register3()
                                        return true
                                    }
                                }
                                Toast.makeText(this,"Pais seleccionado inexistente. Por favor, seleccione los que les recomendamos.\nTenga en cuenta que los paises están en Ingles",Toast.LENGTH_LONG).show()
                                return false
                            }
                        }
                        Toast.makeText(this,"Continente seleccionado inexistente. Por favor, seleccione los que les recomendamos",Toast.LENGTH_SHORT).show()
                        return false
                    }
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
    private fun verifyUserNames(badWords:ArrayList<String>,uName:String, nName:String):Boolean {
        for (badWord in badWords)
        {
            if(badWord == uName.lowercase() || badWord == nName.lowercase())
            {
                return false
            }
        }
        return true
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
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
    }
}