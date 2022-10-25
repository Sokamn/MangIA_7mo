package com.settlet.mangia

import android.app.Dialog
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.settlet.mangia.Adapter.PagerAdapterAS
import com.settlet.mangia.Adapter.PagerAdapterFF
import com.settlet.mangia.Adapter.PagerAdapterP
import com.settlet.mangia.databinding.ActivityAdvancedSearchBinding

class AdvancedSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdvancedSearchBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var filters: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvancedSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        filters = Dialog(this)
        filters.setContentView(R.layout.popup_filter)
        filters.window!!.setGravity(Gravity.RIGHT)
        filters.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        viewPager = findViewById(R.id.vwpContentAS)
        tabLayout = findViewById(R.id.tblTabLayoutAS)
        viewPager.adapter = PagerAdapterAS(this)
        TabLayoutMediator(tabLayout,viewPager){ tab,position->
            tab.icon = when(position){
                0 -> {
                    ContextCompat.getDrawable(this, R.drawable.ic_search_recipe)
                }
                1 -> {
                    ContextCompat.getDrawable(this, R.drawable.ic_search_user)
                }
                else -> throw Resources.NotFoundException("Position Not Found")
            }
        }.attach()

        binding.imbBackAS.setOnClickListener {
            onBackPressed()
        }

        binding.imbOptionsAS.setOnClickListener {
            filters.show()
        }
    }
}