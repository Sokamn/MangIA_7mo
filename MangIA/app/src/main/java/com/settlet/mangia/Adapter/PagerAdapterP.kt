package com.settlet.mangia.Adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.settlet.mangia.Fragment.RecipesFragment
import com.settlet.mangia.Fragment.SavedFragment

class PagerAdapterP(fm: FragmentActivity): FragmentStateAdapter(fm) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> RecipesFragment()
            1 -> SavedFragment()
            else -> throw Resources.NotFoundException("Position Not Found")
        }
    }
}