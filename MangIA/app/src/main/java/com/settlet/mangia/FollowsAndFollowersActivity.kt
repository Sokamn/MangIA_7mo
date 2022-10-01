package com.settlet.mangia

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.Adapter.PagerAdapterFF
import com.settlet.mangia.databinding.ActivityFollowsAndFollowersBinding

class FollowsAndFollowersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFollowsAndFollowersBinding
    private lateinit var viewPager:ViewPager2
    private lateinit var tabLayout:TabLayout
    private var follows = 0
    private var following = 0
    private val reference = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowsAndFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.primaryColor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        viewPager = findViewById(R.id.vwpContentFF)
        tabLayout = findViewById(R.id.tblTabLayoutFF)
        val prefs = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val profileID = prefs.getString("profileID","none")
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
            reference.child("follow").child(profileID.toString()).child("followers").get().addOnSuccessListener{ a->
                reference.child("follow").child(profileID.toString()).child("following").get().addOnSuccessListener{ b->
                    tab.text = when(position){
                        0 -> "${a.childrenCount} Seguidores"
                        1 -> "${b.childrenCount} Seguidos"
                        else -> throw Resources.NotFoundException("Position Not Found")
                    }
                }
            }
        }.attach()

        binding.imbBackFF.setOnClickListener{
            onBackPressed()
            finish()
        }

    }
}