package com.settlet.mangia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.settlet.mangia.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.add(MeowBottomNavigation.Model(0,R.drawable.ic_home_menu))
        binding.bottomNav.add(MeowBottomNavigation.Model(1,R.drawable.ic_scan_nav))
        binding.bottomNav.add(MeowBottomNavigation.Model(2,R.drawable.ic_comment_recipe_black))
        binding.bottomNav.show(2,true)

        binding.bottomNav.setOnClickMenuListener {
            when(it.id){
                0->{
                    onBackPressed()
                }
                1->{
                    this.startActivity(Intent(this, ScanCameraActivity::class.java))
                }
                2->{

                }
                else->{

                }
            }
        }
    }
}