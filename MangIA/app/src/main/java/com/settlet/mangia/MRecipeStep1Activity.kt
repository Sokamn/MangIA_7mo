package com.settlet.mangia

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.databinding.ActivityMrecipeStep1Binding
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_mrecipe_step1.*
import kotlinx.android.synthetic.main.activity_mrecipe_step1.view.*
import kotlinx.android.synthetic.main.row_gallery_recycler.view.*
import java.io.File
import java.lang.Exception
import java.util.jar.Manifest
import kotlin.math.log

class MRecipeStep1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMrecipeStep1Binding
    private var allPictures: ArrayList<Image>?=null
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        val inputUri = uri
        val outputUri = File(filesDir,"croppedImage.jpg").toUri()
        val listUri = listOf<Uri>(inputUri,outputUri)
        cropImage.launch(listUri)
    }
    private val uCropContract = object: ActivityResultContract<List<Uri>,Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f,5f)
                .withMaxResultSize(1080,1080)
            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }
    }
    private val cropImage = registerForActivityResult(uCropContract){ uri ->
        binding.imvImageAdded.setImageURI(uri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        allPictures = ArrayList()
        binding.rcvGaleryMR.layoutManager = GridLayoutManager(this,3)
        binding.rcvGaleryMR.setHasFixedSize(true)


        if(ContextCompat.checkSelfPermission(this@MRecipeStep1Activity,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this@MRecipeStep1Activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),101)
        }

        if(allPictures!!.isEmpty())
        {
            binding.pgbLoadImagesMR.visibility = View.VISIBLE
            allPictures=getAllImages()
            binding.rcvGaleryMR.adapter = ImageAdapter(this,allPictures!!)
            binding.pgbLoadImagesMR.visibility = View.GONE
        }

        binding.imvCloseMR.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.imvNextStepMR.setOnClickListener {
            val intent = Intent(this, MRecipeStep2Activity::class.java)
            startActivity(intent)
        }
        binding.imvImageAdded.setOnClickListener {

        }
        binding.imvMFiles.setOnClickListener {
            getContent.launch("image/*")
        }

    }

    private fun getAllImages(): ArrayList<Image> {
        val images = ArrayList<Image>()
        val allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA,MediaStore.Images.Media.DISPLAY_NAME)
        var cursor = this@MRecipeStep1Activity.contentResolver.query(allImageUri,projection,null,null,null)

        try{
            cursor!!.moveToFirst()
            do {
                val image = Image()
                image.imagePath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                image.imageName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(image)
            }while (cursor.moveToNext())
            cursor.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
        Glide.with(this)
            .load(images[0].imagePath)
            .into(binding.imvImageAdded)
        return images.reversed() as ArrayList<Image>
    }
}