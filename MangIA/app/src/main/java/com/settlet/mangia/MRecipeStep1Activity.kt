package com.settlet.mangia

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.GridLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.settlet.mangia.databinding.ActivityMrecipeStep1Binding
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_mrecipe_step1.*
import java.lang.Exception
import java.util.jar.Manifest

class MRecipeStep1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMrecipeStep1Binding
    private var allPictures: ArrayList<Image>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMrecipeStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcvGaleryMR?.layoutManager = GridLayoutManager(this,3)
        binding.rcvGaleryMR?.setHasFixedSize(true)

        if(ContextCompat.checkSelfPermission(this@MRecipeStep1Activity,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this@MRecipeStep1Activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),101)
        }

        allPictures= ArrayList()

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
        return images
    }
}