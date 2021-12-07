package com

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    val information = arrayListOf("저장한 곡", "음악파일", "저장 앨범")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerViewpagerAdapter (this)
        binding.lockerVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerTb, binding.lockerVp){
            tab, position ->
            tab.text = information[position]
        }.attach()

        binding.lockerLoginTv.setOnClickListener{
            startActivity(Intent(activity,LoginActivity::class.java))
        }

        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getUserIdx()
        val likedAlbums = songDB.AlbumDao().getLikeAlbum(userId)
//
//        Log.d("lockerrag?get_albums",likedAlbums.toString())

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initViews()
    }

    private fun initViews(){
        //jwt를 가져오는 함수
//        val jwt = getJwt()
        val jwt: Int = getUserIdx()
        val userDB = SongDatabase.getInstance(requireContext())!!

        if(jwt==0){
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else {
            binding.lockerLoginTv.text = "로그아웃"
            val user = userDB.UserDao().getUsername(jwt)
            binding.lockerUsernameTv.text = user?.username
            binding.lockerLoginTv.setOnClickListener {
                //로그아웃을 시켜주는 함수
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun getJwt(): Int{
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("id", 0)
    }

    private fun getUserIdx(): Int{
        val spf = activity?.getSharedPreferences("auth",MODE_PRIVATE)

        return spf!!.getInt("userid", 0)
    }

    private fun logout(){
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("jwt")
        editor.apply()
    }
}