package com

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerViewpagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
//인자를 써야함.

    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragmentlist.size

    override fun createFragment(position: Int): Fragment =fragmentlist[position]

    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size - 1)
        //viewpager한테 새로운 인자가 있으니 프래그먼트에 보여줘 라는 기능
    }
}
