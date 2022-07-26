package com.settlet.mangia

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.settlet.mangia.Adapter.PagerAdapterFF
import com.settlet.mangia.databinding.ActivityFollowsAndFollowersBinding

class FollowsAndFollowersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFollowsAndFollowersBinding
    private lateinit var viewPager:ViewPager2
    private lateinit var tabLayout:TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowsAndFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        viewPager = findViewById(R.id.vwpContentFF)
        tabLayout = findViewById(R.id.tblTabLayoutFF)
        val extra = intent.extras!!.getString("vPage").toString()

        viewPager.adapter = PagerAdapterFF(this)

        when (extra) {
            "Follows" -> {
                viewPager.currentItem = 1
                Log.d("EXTRAS", "Follows")
            }
            "Followers" -> {
                viewPager.currentItem = 0
                Log.d("EXTRAS", "Followers")
            }
            else -> {
                viewPager.currentItem = 0
                Log.d("EXTRAS", "else")
            }
        }

        TabLayoutMediator(tabLayout,viewPager){ tab,position->
            tab.text = when(position){
                0 -> "Seguidores"
                1 -> "Seguidos"
                else -> throw Resources.NotFoundException("Position Not Found")
            }
        }.attach()

        binding.imbBackFF.setOnClickListener{
            onBackPressed()
            finish()
        }

    }
}