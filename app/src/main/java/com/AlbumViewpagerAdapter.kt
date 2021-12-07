package com

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumViewpagerAdapter (fragment : Fragment) : FragmentStateAdapter(fragment){

    override fun getItemCount(): Int =3
    // 수록곡, 영상, 상세정보 3개 > 프래그먼트 갯수도 3개

    override fun createFragment(position: Int): Fragment {
        return when(position){
            //when = switch문의 역할을 하는 것 : 어떤 값 할당시, if문같은거
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragmet()
        }
    }
}