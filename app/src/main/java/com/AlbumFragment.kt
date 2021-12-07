package com

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.R
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding : FragmentAlbumBinding
    private var gson : Gson = Gson()
    //전역변수 설정
    val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //초기화하는 하나의 패턴
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        //home에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        isLiked = isLikeAlbum(album.id)

        setViews(album)
        setClickListeners(album)

        //ROOM_DB
        //앨범안에 있는 수록곡들 불러오기
        val songs = getSongs(album.id)
        // 이 다음에 수록곡 프래그먼트에 songs을 전달해주는 식으로 사용하시면 됩니다.

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        //아래는 어댑터로 연결하는 작업을 해주는 것.
        val albumAdapter = AlbumViewpagerAdapter(this)

        binding.albumContentVp.adapter = albumAdapter

        //탭 레이아웃과 뷰페이저 연결
        //연결될 탭의 인자값=연결할 탭레이아웃과 연결할뷰페이저입력
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
            tab, position ->
            //포지션에 따른 텍스트 설정
            tab.text = information[position]
        }.attach()
        //attach 함수 사용..

        return binding.root
    }

    private fun setViews(album: Album) {
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumMusicSingerTv.text = album.singer.toString()
        binding.albumMusicInfoTv.text = album.albuminfo.toString()
        binding.albumAlbumIv.setImageResource(album.coverImg!!)

        if(isLiked){
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album: Album){
        val userId: Int = getJwt()

        binding.albumLikeIv.setOnClickListener {
            if(isLiked){
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                //테이블의 앨범 삭제
                Toast.makeText(getActivity(),"저장 앨범이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                disLikeAlbum(userId, album.id)
            } else {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                Toast.makeText(getActivity(),"앨범이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                likeAlbum(userId, album.id)
            }
        }
    }

    //앨범 좋아요 눌렀을때 DB에 저장
    private fun likeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        //좋아요 눌렀을때 유저와 앨범 정보 둘 다 저장
        val like = Like(userId, albumId)

        songDB.AlbumDao().likeAlbum((like))
    }

    private fun isLikeAlbum(albumId: Int): Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        //이미 좋아요가 눌러져있는지 확인해주는 함수
        val likeId: Int? = songDB.AlbumDao().isLikeAlbum(userId,albumId)

        return likeId != null
    }

    //테이블에서 정보를 삭제
    private fun disLikeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.AlbumDao().disLikeAlbum(userId, albumId)
    }

    private fun getJwt(): Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt", 0)
    }

    //ROOM_DB
    private fun getSongs(albumIdx: Int): ArrayList<Song>{
        val songDB = SongDatabase.getInstance(requireContext())!!

        val songs = songDB.SongDao().getSongsInAlbum(albumIdx) as ArrayList

        return songs
    }

}