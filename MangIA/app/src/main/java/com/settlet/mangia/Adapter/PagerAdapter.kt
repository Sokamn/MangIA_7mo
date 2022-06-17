package com.settlet.mangia.Adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.settlet.mangia.Fragment.FollowersFragment
import com.settlet.mangia.Fragment.FollowsFragment

class PagerAdapter(fm:FragmentActivity): FragmentStateAdapter(fm) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int):Fragment{
        return when (position){
            0 -> FollowersFragment()
            1 -> FollowsFragment()
            else -> throw Resources.NotFoundException("Position Not Found")
        }

    }

}