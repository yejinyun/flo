package com

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PanelViewpagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> Panel01Fragment()
            1 -> Panel02Fragment()
            2 -> Panel03Fragment()
            else -> Panel04Fragment()
        }
    }


}