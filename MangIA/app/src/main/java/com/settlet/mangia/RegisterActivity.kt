package com.settlet.mangia

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.blongho.country_data.World
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Model.DatePickerFragment
import com.settlet.mangia.Provider.countryProvider
import com.settlet.mangia.databinding.ActivityRegisterBinding
import java.util.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private val db = Firebase.firestore
    private val reference = FirebaseDatabase.getInstance().reference
    private var contador: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        World.init(applicationContext)
        val continents = resources.getStringArray(R.array.continents)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val wordtoSpan: Spannable = SpannableString(binding.txvPLoginR.text)

        wordtoSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.secundaryColor)),
            19,
            binding.txvPLoginR.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txvPLoginR.text = wordtoSpan

        val pNumber = intent.getStringExtra("pNumber").toString()
        val nName = intent.getStringExtra("nName").toString()
        val gMail = intent.getStringExtra("email").toString()
        val loginMethod = intent.getStringExtra("logIn").toString()
        val photoProfile = intent.getStringExtra("photoProfile").toString()

        if(loginMethod=="Google"){
            if (pNumber!="null"&&pNumber.isNotEmpty()){
                binding.txpTelR.setText(pNumber)
            }
            if(nName.isNotEmpty()){
                binding.txpNNameR.setText(nName)
            }
            if(gMail.isNotEmpty()){
                binding.txpMailR.setText(gMail)
                binding.txpMailR.inputType = InputType.TYPE_NULL
                binding.txpMailR.isEnabled = false
                binding.txpMailR.isFocusableInTouchMode = false
            }
            binding.txpPassR.isFocusableInTouchMode = false
            binding.txpPassR.isEnabled = false
            binding.txpPassR.isCursorVisible = false
            binding.txpPassR.keyListener = null
            binding.txpRepeatPassR.isFocusableInTouchMode = false
            binding.txpRepeatPassR.isEnabled = false
            binding.txpRepeatPassR.isCursorVisible = false
            binding.txpRepeatPassR.keyListener = null
        }


        val arrayAdapterC = ArrayAdapter<String>(this, R.layout.spinner_ubication_item, continents)
        val arrayAdapterLAfrica = ArrayAdapter<String>(this,R.layout.spinner_ubication_item, countryProvider.lAfrican)
        val arrayAdapterLAsia = ArrayAdapter<String>(this,R.layout.spinner_ubication_item, countryProvider.lAsia)
        val arrayAdapterLASouth = ArrayAdapter<String>(this,R.layout.spinner_ubication_item, countryProvider.lSAmerica)
        val arrayAdapterLANorth = ArrayAdapter<String>(this,R.layout.spinner_ubication_item, countryProvider.lNAmerica)
        val arrayAdapterLOceania = ArrayAdapter<String>(this,R.layout.spinner_ubication_item, countryProvider.lOceania)
        val arrayAdapterLEurope = ArrayAdapter<String>(this,R.layout.spinner_ubication_item, countryProvider.lEurope)

        binding.txpRegionR.adapter = arrayAdapterC

        binding.txpRegionR.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0 != null) {
                    when(binding.txpRegionR.selectedItem.toString()){
                        "Africa"->{
                            binding.txpCountryR.adapter = arrayAdapterLAfrica
                        }
                        "Asia"->{
                            binding.txpCountryR.adapter = arrayAdapterLAsia
                        }
                        "America del Norte o Central"->{
                            binding.txpCountryR.adapter = arrayAdapterLANorth
                        }
                        "America del Sur"->{
                            binding.txpCountryR.adapter = arrayAdapterLASouth
                        }
                        "Europa"->{
                            binding.txpCountryR.adapter = arrayAdapterLEurope
                        }
                        "Oceania"->{
                            binding.txpCountryR.adapter = arrayAdapterLOceania
                        }
                        else ->{
                            binding.txpCountryR.adapter = null
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        } 




        binding.txpDateBirthR.setOnClickListener { showDatePickerDialog() }
        binding.btnContinueR.setOnClickListener{ // Boton de continuar inicial
            if(checkValue(contador)) {
                if (loginMethod!="Email"&&contador==1) {
                    contador++
                    register4()
                }
                contador++
                Log.d("NUM",contador.toString())
                Log.d("NUMB",contador.toString())
            }
        }
        binding.imbBackR.setOnClickListener{ // Boton de volver
            checkBack(contador,loginMethod)
            if (contador==3){
                contador--
            }
            contador--
            Log.d("NUMBBACK", contador.toString())
        }
        binding.txvPLoginR.setOnClickListener { // Volver al Login
            onBackPressed()
            Firebase.auth.signOut()
        }
        binding.btnContinueR2.setOnClickListener{ // Boton de finalizar registro
            val mailr = binding.txpMailR.text.toString()
            val passr = binding.txpPassR.text.toString()
            if(loginMethod=="Email"){
                createAccount(mailr,passr)
            }else{
                val docUser = hashMapOf("age" to Calendar.getInstance().get(Calendar.YEAR)-binding.txpDateBirthR.text.substring(binding.txpDateBirthR.text.length-4).trim().toInt(),
                    "userID" to auth.currentUser!!.uid,
                    "biography" to "",
                    "cantReports" to 0,
                    "country" to binding.txpCountryR.selectedItem.toString(),
                    "dateBirth" to binding.txpDateBirthR.text.toString(),
                    "dateCreationAccount" to Calendar.getInstance().time.toString(),
                    "email" to binding.txpMailR.text.toString(),
                    "nickName" to binding.txpNNameR.text.toString(),
                    "password" to passr,
                    "phoneNumber" to binding.txpTelR.text.toString(),
                    "region" to binding.txpRegionR.selectedItem.toString(),
                    "userName" to binding.txpUserNameR.text.toString()
                )
                reference.child("users").child(auth.currentUser!!.uid).setValue(docUser).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        binding.btnCancelR.setOnClickListener { // Boton para cancelar registro ( NO ACEPTA TERMINOS DE CONDICIONES )
            onBackPressed()
            Firebase.auth.signOut()
        }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                val lMethod = intent.getStringExtra("logIn").toString()
                if (lMethod != "Google"){
                    reload()
                }
            } else{
                val intent = Intent(this, CheckMailActivity::class.java)
                startActivity(intent)
                finish()
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
                    val docUser = hashMapOf("age" to Calendar.getInstance().get(Calendar.YEAR)-binding.txpDateBirthR.text.substring(binding.txpDateBirthR.text.length-4).trim().toInt(),
                        "userID" to auth.currentUser!!.uid,
                        "biography" to "",
                        "cantReports" to 0,
                        "country" to binding.txpCountryR.selectedItem.toString(),
                        "dateBirth" to binding.txpDateBirthR.text.toString(),
                        "dateCreationAccount" to Calendar.getInstance().time.toString(),
                        "email" to binding.txpMailR.text.toString(),
                        "nickName" to binding.txpNNameR.text.toString(),
                        "password" to password,
                        "phoneNumber" to binding.txpTelR.text.toString(),
                        "region" to binding.txpRegionR.selectedItem.toString(),
                        "userName" to binding.txpUserNameR.text.toString()
                    )
                    reference.child("users").child(auth.currentUser!!.uid).setValue(docUser).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this, CheckMailActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Error al registrarse. Por favor, intentelo más tarde.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun checkValue(count:Int):Boolean{
        val passwordRegex = Pattern.compile("^" + "(?=.*[A-Z])" + "(?=.*[0-9])" + ".{6,}" + "$")
        val badWords = arrayListOf<String>("sorete","imbecil","tarado","pelotudo","pajero","pajera","pelotuda","tarada","puto","puta","concha","culo","poronga","verga","pito","pene" + "nigga" , "trola" , "trolo" , "caca" , "down" , "mierda" , "nazi" , "hitler" , "estupido" , "coger" , "cojer" , "pendejo " , "pendeja" , "porno" , "orto" , "sexo" , "pinche" , "pinchi" , "cojo" , "cabrón" , "cabrona" , "mames" , "pendejos" , "pendejas" , "chinga" , "mamadas" , "pendejadas" , "mama huevo" , "pete" , "wueon" , "xuxa" , "weon" , "weonado" , "weona" , "coño" , "aguevoniado" , "guevon" , "pajuo" , "marica", "monda" , "marrana" , "marrano" ,"monda" , "pijudo" , "hijueputa" , "cotopla" , "pichurria" , "picha" , "mother fucker" , "fuck" , "ass" , "orgy" , "bitch" , "suck" , "my balls" , "slut " , "whore" , "hoe" , "chupamela" , "culito" , "cojida" , "cojiendo" , "zoofilia" , "putito" , "reputo" , "free viagra" , "taradito", "taradita" , "pelotudito" , "pelotudita" , "pelotuditos", "pelotuditas" , "putita" , "poronguita" , "verguita" , "pitito" , "trolito" , "trolita" , "caquita" , "estupidito" , "estupidita" , "pendejito" , "pendejita" , "putitos" , "putitas" , "poronguitas" , "porongotas" , "porongota" , "porongon" , "verguitas", "vergotas" , "vergota" , "pititos" , "pitotes" , "pitote" , "trolitos" , "trolitas" , "caquitas" , "cacotas" , "estupiditos" , "estupiditas" , "pendejitos" , "pendejitas" , "feto" , "cigoto" , "caka" , "kaka" , "kk" , "joto" , "jota" , "kaco" , "kago" , "kojo" , "kulo" , "mamo" , "meaas" , "mion" , "mula" , "pedo" , "qulo" , "buey" , "caco" , "cago" , "cako" , "coja" , "coji" , "guey" , "kaca" , "kaga" , "koge" , "mame" , "mear" , "meon" , "moco")
        when(count){
            0 -> {
                if(binding.txpMailR.text.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(binding.txpMailR.text.toString()).matches())
                {
                    Toast.makeText(this,"Ingrese un mail valido.",Toast.LENGTH_SHORT).show()
                    return false
                }
                if(binding.txpTelR.text.isNotEmpty()&&binding.txpUserNameR.text.isNotEmpty()&&binding.txpNNameR.text.isNotEmpty()){
                    if(verifyUserNames(badWords,binding.txpUserNameR.text.toString(),binding.txpNNameR.text.toString()))
                    {
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
                if(binding.txpCountryR.selectedItem.toString().isNotEmpty()&&binding.txpRegionR.selectedItem.toString().isNotEmpty()&&binding.txpDateBirthR.text.isNotEmpty()){
                    val edad = Calendar.getInstance().get(Calendar.YEAR)-binding.txpDateBirthR.text.substring(binding.txpDateBirthR.text.length-4).trim().toInt()
                    if(edad < 13)
                    {
                        Toast.makeText(this, "Usted tiene menos de 13 años, por favor, ingrese una fecha valida.",Toast.LENGTH_SHORT).show()
                        return false
                    }else{
                        for (c in resources.getStringArray(R.array.continents))
                        {
                            if(binding.txpRegionR.selectedItem.toString() == c)
                            {
                                for (C in countryProvider.lTPaises)
                                {
                                    if(binding.txpCountryR.selectedItem.toString() == C)
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
    private fun checkBack(contador: Int,loginMethod:String){//si contador es 0 se vuelve al intent de Login, si es 1 vuelve al registro 1
        when(contador)
        {
            0 ->{
                onBackPressed()
                Firebase.auth.signOut()

            }
            1 ->{register1()}
            2 ->{register2()}
            3 ->{
                if (loginMethod!=""){
                    register2()
                }
                else{
                    register3()
                }
            }
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
        binding.txpMailR.visibility = View.VISIBLE
        binding.txpTelR.visibility = View.VISIBLE
        binding.txpUserNameR.visibility = View.VISIBLE
        binding.txpNNameR.visibility = View.VISIBLE
        //register2
        binding.txpDateBirthR.visibility = View.INVISIBLE
        binding.txpRegionR.visibility = View.INVISIBLE
        binding.txpCountryR.visibility = View.INVISIBLE
        binding.lineSpnRegionR.visibility= View.INVISIBLE
        binding.lineSpnCountryR.visibility= View.INVISIBLE
        //register3
        binding.txpPassR.visibility = View.INVISIBLE
        binding.txpRepeatPassR.visibility = View.INVISIBLE
        //register4
        binding.btnContinueR.visibility = View.VISIBLE
        binding.btnContinueR2.visibility = View.INVISIBLE
        binding.btnCancelR.visibility = View.INVISIBLE
        binding.txvTerminos.visibility = View.INVISIBLE
    }
    private fun register2(){
        //register1
        binding.txpMailR.visibility = View.INVISIBLE
        binding.txpTelR.visibility = View.INVISIBLE
        binding.txpUserNameR.visibility = View.INVISIBLE
        binding.txpNNameR.visibility = View.INVISIBLE
        //register2
        binding.txpDateBirthR.visibility = View.VISIBLE
        binding.txpRegionR.visibility = View.VISIBLE
        binding.txpCountryR.visibility = View.VISIBLE
        binding.lineSpnRegionR.visibility= View.VISIBLE
        binding.lineSpnCountryR.visibility= View.VISIBLE
        //register3
        binding.txpPassR.visibility = View.INVISIBLE
        binding.txpRepeatPassR.visibility = View.INVISIBLE
        //register4
        binding.btnContinueR.visibility = View.VISIBLE
        binding.btnContinueR2.visibility = View.INVISIBLE
        binding.btnCancelR.visibility = View.INVISIBLE
        binding.txvTerminos.visibility = View.INVISIBLE
    }
    private fun register3(){
        //register1
        binding.txpMailR.visibility = View.INVISIBLE
        binding.txpTelR.visibility = View.INVISIBLE
        binding.txpUserNameR.visibility = View.INVISIBLE
        binding.txpNNameR.visibility = View.INVISIBLE
        //register2
        binding.txpDateBirthR.visibility = View.INVISIBLE
        binding.txpRegionR.visibility = View.INVISIBLE
        binding.txpCountryR.visibility = View.INVISIBLE
        binding.lineSpnRegionR.visibility= View.INVISIBLE
        binding.lineSpnCountryR.visibility= View.INVISIBLE
        //register3
        binding.txpPassR.visibility = View.VISIBLE
        binding.txpRepeatPassR.visibility = View.VISIBLE
        //register4
        binding.btnContinueR.visibility = View.VISIBLE
        binding.btnContinueR2.visibility = View.INVISIBLE
        binding.btnCancelR.visibility = View.INVISIBLE
        binding.txvTerminos.visibility = View.INVISIBLE
    }
    private fun register4(){
        //register1
        binding.txpMailR.visibility = View.INVISIBLE
        binding.txpTelR.visibility = View.INVISIBLE
        binding.txpUserNameR.visibility = View.INVISIBLE
        binding.txpNNameR.visibility = View.INVISIBLE
        //register2
        binding.txpDateBirthR.visibility = View.INVISIBLE
        binding.txpRegionR.visibility = View.INVISIBLE
        binding.txpCountryR.visibility = View.INVISIBLE
        binding.lineSpnRegionR.visibility= View.INVISIBLE
        binding.lineSpnCountryR.visibility= View.INVISIBLE
        //register3
        binding.txpPassR.visibility = View.INVISIBLE
        binding.txpRepeatPassR.visibility = View.INVISIBLE
        //register4
        binding.btnContinueR.visibility = View.INVISIBLE
        binding.btnContinueR2.visibility = View.VISIBLE
        binding.btnCancelR.visibility = View.VISIBLE
        binding.txvTerminos.visibility = View.VISIBLE
    }
    private fun reload()
    {
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        finish()
    }
}