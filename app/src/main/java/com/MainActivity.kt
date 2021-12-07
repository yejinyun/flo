package com

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.*
import com.example.flo.R
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var song: Song

    //Gson
    private var gson : Gson = Gson()

    //미디어 플레이어
    private var mediaPlayer:MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        inputDummyAlbums()
        inputDummySongs()

        binding.mainPlayerLayout.setOnClickListener {
            Log.d("nowSongId", song.id.toString())
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this@MainActivity, SongActivity::class.java)

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!
        song = if (songId == 0) {
            songDB.SongDao().getSong(1)
        } else {
            songDB.SongDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }
    }

    private fun setMiniPlayer(song: Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainSeekbar.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        mediaPlayer = MediaPlayer.create(this, music)

        if (song.isPlaying){
            binding.mainMiniplayerpauseBtn.visibility = View.VISIBLE
            binding.mainMiniplayerplayBtn.visibility = View.GONE
        } else {
            binding.mainMiniplayerpauseBtn.visibility = View.GONE
            binding.mainMiniplayerplayBtn.visibility = View.VISIBLE
        }
    }

    //ROOM_DB
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.AlbumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.AlbumDao().insert(Album(1,"Panda Bear","혁오","언제였지|싱글|인디",R.drawable.img_album_panda,false))
        songDB.AlbumDao().insert(Album(2, "반복", "한요한", "2020.02.08｜싱글｜힙합", R.drawable.img_album_yohan,false))
        songDB.AlbumDao().insert(Album(3, "가을밤에 든 생각", "잔나비", "2020.11.06｜미니｜인디 락", R.drawable.album_jannabi,false))
        songDB.AlbumDao().insert(Album(4, "Sketch", "정동하","2019.09.17｜미니｜발라드", R.drawable.img_album_sketch,false))
        songDB.AlbumDao().insert(Album(5, "Butter", "방탄소년단 (BTS)", "2021.08.27｜싱글｜댄스 팝", R.drawable.img_album_exp,false))
        songDB.AlbumDao().insert(Album(6, "IU 5th ALbum 'LILAC'", "아이유 (IU)", "2021.03.25｜정규｜댄스 팝", R.drawable.img_album_exp2,false))

    }

    private fun inputDummySongs(){
        //DB 인스턴스 - 이 액티비티로부터 데이터베이스를 얻어온다.
        val songDB = SongDatabase.getInstance(this)!!
        //테이블의 모든 데이터
        val songs = songDB.SongDao().getSongs()

        if(songs.isNotEmpty()) return

        songDB.SongDao().insert(Song("Panda Bear", "혁오", 0,30, false,"music_jannabi", R.drawable.img_album_panda, "",false))
        songDB.SongDao().insert(Song("반복", "한요한",  0,30, false,"music_jannabi", R.drawable.img_album_yohan,"",false))
        songDB.SongDao().insert(Song("가을밤에 든 생각", "잔나비",  0,30, false,"music_jannabi", R.drawable.album_jannabi,"",false))
        songDB.SongDao().insert(Song("Sketch", "정동하", 0,30, false,"music_jannabi", R.drawable.img_album_sketch, "",false))
        songDB.SongDao().insert(Song("Butter", "방탄소년단 (BTS)",  0,30, false,"music_jannabi", R.drawable.img_album_exp, "",false))
        songDB.SongDao().insert(Song("IU 5th Album 'LILAC'", "아이유 (IU)",  0,30, false,"music_lilac", R.drawable.img_album_exp2,"",false))

        val _songs = songDB.SongDao().getSongs()
        Log.d("DB Data", _songs.toString())
    }

}