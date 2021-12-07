package com

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.*
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albums = ArrayList<Album>();

    private lateinit var songDB: SongDatabase

    val information = arrayListOf("●", "●", "●", "●")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        //데이터 리스트 생성(더미데이터)
        //실제로는 서버랑 데이터를 주고받음
//        albumDatas.apply {
//            add(Album("Panda Bear", "혁오", "2015.01.21｜싱글｜인디 락", R.drawable.img_album_panda))
//            add(Album("반복", "한요한", "2020.02.08｜싱글｜힙합", R.drawable.img_album_yohan))
//            add(Album("가을밤에 든 생각", "잔나비", "2020.11.06｜미니｜인디 락", R.drawable.album_jannabi))
//            add(Album("Sketch", "정동하","2019.09.17｜미니｜발라드", R.drawable.img_album_sketch))
//            add(Album("Butter", "방탄소년단 (BTS)", "2021.08.27｜싱글｜댄스 팝", R.drawable.img_album_exp))
//            add(Album("IU 5th ALbum 'LILAC'", "아이유 (IU)", "2021.03.25｜정규｜댄스 팝", R.drawable.img_album_exp2))
//        }

        //ROOM_DB
        songDB = SongDatabase.getInstance(requireContext())!!
        // songDB에서 album list 가져와서 화면에 띄운다.
        albums.addAll(songDB.AlbumDao().getAlbums())

        //레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        //어댑터 설정 : 더미데이터랑 어댑터 연결
        val albumRVAdapter = AlbumRVAdapter(albums)
        //리사이클러뷰에 어댑터를 연결
        binding.homeTodayMusicAlbumRecyclerView.adapter = albumRVAdapter

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{

            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                TODO("Not yet implemented")
//                albumRVAdapter.removeItem(position)
            }
        })
        //레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            //수평으로 리니어레이아웃

        val bannerAdapter = BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp3))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        //뷰페이저와 프래그먼트 연결
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val panelAdapter = PanelViewpagerAdapter (this)
        binding.homePanelVp.adapter = panelAdapter

        TabLayoutMediator(binding.homePanelTb, binding.homePanelVp){
                tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }
}