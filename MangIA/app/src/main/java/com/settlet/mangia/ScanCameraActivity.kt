package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.settlet.mangia.databinding.ActivityScanCameraBinding

class ScanCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding.bottomNav.add(MeowBottomNavigation.Model(0,R.drawable.ic_home_menu))
        binding.bottomNav.add(MeowBottomNavigation.Model(1,R.drawable.ic_scan_nav))
        binding.bottomNav.add(MeowBottomNavigation.Model(2,R.drawable.ic_comment_recipe_black))
        binding.bottomNav.show(1,true)

        binding.bottomNav.setOnClickMenuListener {
            when(it.id){
                0->{
                    this.startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                1->{

                }
                2->{
                    this.startActivity(Intent(this, ChatActivity::class.java))
                    finish()
                }
                else->{

                }
            }
        }
    }
}