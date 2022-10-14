package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.settlet.mangia.databinding.ActivityChatBinding
import com.settlet.mangia.databinding.ActivitySplashScanBinding

class ScanCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.add(MeowBottomNavigation.Model(0,R.drawable.ic_home_menu))
        binding.bottomNav.add(MeowBottomNavigation.Model(1,R.drawable.ic_scan_nav))
        binding.bottomNav.add(MeowBottomNavigation.Model(2,R.drawable.ic_comment_recipe_black))
        binding.bottomNav.show(1,true)

        binding.bottomNav.setOnClickMenuListener {
            when(it.id){
                0->{
                    onBackPressed()
                }
                1->{

                }
                2->{
                    Toast.makeText(baseContext,"Chat", Toast.LENGTH_SHORT).show()
                }
                else->{

                }
            }
        }
    }
}