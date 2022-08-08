package com.settlet.mangia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.settlet.mangia.Adapter.ImageAdapter
import com.settlet.mangia.Adapter.SliderAdapter
import com.settlet.mangia.Model.Image
import com.settlet.mangia.databinding.ActivityMrecipeStep1Binding
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MRecipeStep1Activity : AppCompatActivity() {
    internal lateinit var binding: ActivityMrecipeStep1Binding
    internal var i = 0
    private var allPictures: ArrayList<Image>?=null
    internal var isMultiImages: Boolean = false
    internal var uniqueImage:String = ""
    internal var imageList: MutableList<String> = mutableListOf()
    private lateinit var photoFile: File
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            val intent = it.data
            val imgBitmap = intent?.extras?.get("data")
            val inputUri = photoFile.toUri()
            while(File(filesDir,"croppedImage${i}.jpg").exists())
            {
                i++
            }
            val outputUri = File(filesDir,"croppedImage${i}.jpg").toUri()
            val listUri = listOf<Uri>(inputUri,outputUri)
            cropImage.launch(listUri)
        }
    }
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri!=null)
        {
            val inputUri = uri
            while(File(filesDir,"croppedImage${i}.jpg").exists())
            {
                i++
            }
            val outputUri = File(filesDir,"croppedImage${i}.jpg").toUri()
            val listUri = listOf<Uri>(inputUri,outputUri)
            cropImage.launch(listUri)
        }
        else{
            Toast.makeText(baseContext,"No has seleccionado ninguna imagen.",Toast.LENGTH_SHORT).show()
        }
    }
    private val uCropContract = object: ActivityResultContract<List<Uri>,Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]
            Log.d("URI","${input[0]} ! ${input[1]}")

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f,5f)
                .withMaxResultSize(1080,1080)
            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if(intent!=null)
            {
                return UCrop.getOutput(intent)!!
            }
            else
            {
                return null
            }
        }
    }
    private val cropImage = registerForActivityResult(uCropContract){ uri ->
        if (uri!=null)
        {
            if(isMultiImages)
            {
                if(imageList.contains("doesntexist"))
                {
                    imageList.remove("doesntexist")
                }
                Log.d("LIST", imageList.toString())
                imageList.add(uri.toString())
                Log.d("LIST", imageList.toString())
                binding.imgsldrCarruselMR1.setSliderAdapter(SliderAdapter(imageList,true))
            }
            else{
                binding.imvImageAdded.setImageURI(null)
                Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .into(binding.imvImageAdded)
                uniqueImage = uri.toString()
            }
        }
        else{
            Toast.makeText(baseContext,"No has terminado de recortar una imagen.",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        photoFile = getPhotoFile(FILE_NAME)
        allPictures = ArrayList()
        binding.rcvGaleryMR.layoutManager = GridLayoutManager(this, 3)
        binding.rcvGaleryMR.setHasFixedSize(true)
        imageList.add("doesntexist")
        binding.imgsldrCarruselMR1.setSliderAdapter(SliderAdapter(imageList,true))


        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),101)
            }
        }

        if(allPictures!!.isEmpty())
        {
            allPictures=getAllImages()
            if(allPictures!=null){
                binding.rcvGaleryMR.adapter = ImageAdapter(this,allPictures!!)
            }else{
                Toast.makeText(this, "No puedes crear una receta sin imagenes en tu galería.",Toast.LENGTH_SHORT).show()
                onBackPressed()
                finish()
            }
        }

        binding.imvCloseMR.setOnClickListener {
            onBackPressed()
            finish()
        }
        binding.imvCameraMR.setOnClickListener {
            takePicture()
        }

        binding.imvNextStepMR.setOnClickListener {
            val intent = Intent(this, MRecipeStep2Activity::class.java)
            var count = 0
            intent.putExtra("isMultiImages",isMultiImages)
            if(isMultiImages){
                if(imageList.contains("doesntexist")){
                    Toast.makeText(this,"Seleccione una imagen para realizar la publicacion.",Toast.LENGTH_SHORT).show()
                }
                else{
                    if(imageList.count()==1){
                        Toast.makeText(this,"Le recomendamos utilizar el modo 'Imagen Unica' para publicaciones de una sola imagen.",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        intent.putExtra("cant",imageList.count())
                        for (i in imageList)
                        {
                            count++
                            intent.putExtra("image$count",i)
                        }
                        startActivity(intent)
                    }
                }
            }
            else{
                intent.putExtra("uniqueImage",uniqueImage)
                startActivity(intent)
            }
        }

        binding.imvMFiles.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.imvMultiFiles.setOnClickListener {
            isMultiImages = !isMultiImages
            if (isMultiImages)
            {
                imageList.clear()
                imageList.add("doesntexist")
                binding.imgsldrCarruselMR1.setSliderAdapter(SliderAdapter(imageList,true))
                binding.imgsldrCarruselMR1.visibility = View.VISIBLE
                binding.imvImageAdded.visibility = View.INVISIBLE
                binding.imvMultiFiles.setImageResource(R.drawable.backgroundnav)
            }
            else
            {
                binding.imgsldrCarruselMR1.visibility = View.GONE
                binding.imvImageAdded.visibility = View.VISIBLE
                binding.imvMultiFiles.setImageResource(R.drawable.myfiles_expand)
            }
        }
    }

    private fun takePicture() {
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createPhotoFile()
        val fileProvider = FileProvider.getUriForFile(this, "com.settlet.mangia.fileprovider",photoFile)
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
        startForResult.launch(intentCamera)
    }

    private fun createPhotoFile(): File {
        val timeStamp:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}",".jpg",storageDir.apply {

        })
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,".jpg", storageDirectory)
    }

    private fun getAllImages(): ArrayList<Image>? {
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
        if (images.isNotEmpty()){
            Glide.with(this)
                .load(images.last().imagePath)
                .centerCrop()
                .into(binding.imvImageAdded)
            uniqueImage = Uri.fromFile(File(images.last().imagePath!!)).toString()
            Log.d("cantImages",images.count().toString())
            return images.reversed() as ArrayList<Image>
        }
        else{
            Glide.with(this)
                .load(R.drawable.profile_picture)
                .centerCrop()
                .into(binding.imvImageAdded)
            return null
        }
    }
    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    companion object {
        private const val FILE_NAME = "photo.jpg"
    }
}