package com

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerSavedalbumBinding

class SavedAlbumFragment : Fragment(){
    lateinit var binding: FragmentLockerSavedalbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedalbumBinding.inflate(inflater, container, false)

        //인스턴스화
        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedalbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //리스너 객체 생성 및 전달
        val albumRVAdapter = SavedAlbumRVAdapter()

        albumRVAdapter.setMyItemClickListener(object : SavedAlbumRVAdapter.MyItemClickListener {
            override fun onRemoveSavedalbum(songId: Int) {
                albumDB.AlbumDao().getLikeAlbum(getJwt())
            }
        })
        binding.lockerSavedalbumRv.adapter = albumRVAdapter

        albumRVAdapter.addAlbums(albumDB.AlbumDao().getLikeAlbum(getJwt()) as ArrayList)
    }

    private fun getJwt() : Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val jwt = spf!!.getInt("jwt",0)
        Log.d("MAIN_ACT/GET_JWT","jwt_token: $jwt")

        return jwt
    }
}