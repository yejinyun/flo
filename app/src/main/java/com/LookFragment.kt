package com

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLookBinding


class LookFragment : Fragment(), LookView {
    //액티비티 응답 받기
    private lateinit var binding: FragmentLookBinding
    private lateinit var songRVAdapter: SongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initRecyclerView()
        getSongs()
    }

    private fun  initRecyclerView(){
        songRVAdapter = SongRVAdapter(requireContext())
        binding.lookChartRv.adapter = songRVAdapter
        binding.lookChartRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
    }

    //song API 호출
    private fun getSongs() {
        val songService = SongService()
        //lookview가 안에 있어서 this로 받는다.
        songService.setLookView(this)

        songService.getSongs()
    }
    override fun onGetSongsLoading() {
        binding.lookLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongsSuccess(songs: ArrayList<Song>) {
        binding.lookLoadingPb.visibility = View.GONE

        //리사이클러뷰와 연동
        songRVAdapter.addSongs(songs)
    }

    override fun onGetSongsFailure(code: Int, message: String) {
        binding.lookLoadingPb.visibility = View.GONE
        when(code) {
            400 -> Log.d("LookError/API-ERROR", message)
        }
    }

}