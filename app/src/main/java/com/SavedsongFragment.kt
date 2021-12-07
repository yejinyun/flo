package com

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedsongBinding

class SavedsongFragment : Fragment() {
    lateinit var binding: FragmentSavedsongBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedsongRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SongRVAdapter(requireContext())
        //리스너 객체 생성 및 전달

        songRVAdapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener {
            override fun onRemoveSavedsong(songId: Int) {
                songDB.SongDao().updateIsLikeById(false,songId)
            }
        })

        binding.lockerSavedsongRecyclerView.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.SongDao().getLikedSongs(true) as ArrayList)
    }
}