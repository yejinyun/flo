package com

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.commit
//import androidx.fragment.app.replace
import com.example.flo.R
import com.example.flo.databinding.ActivitySongBinding
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class SongActivity : AppCompatActivity() {
    //소괄호:클래스를 다른 클래스로 상속할때는 소괄호를 넣어줘야 한다.
    lateinit var binding: ActivitySongBinding
    //데이터 렌더링하려면 일단 클래스를 전역변수로 만들어줘야 함
    lateinit var timer : Timer

    //미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null

    private var nowPos = 0
    private lateinit var songDB: SongDatabase
    private var songs = ArrayList<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        //activity_song에 있는 최상단 뷰를 마음대로 쓸거야..
        setContentView(binding.root)
        initPlayList()

        initSong()
        initClickListener()

        binding.songListIb.setOnClickListener {
            finish()
//
//            supportFragmentManager.commit {
//                replace<LockerFragment>(R.id.main_frm)
//                setReorderingAllowed(true)
//                addToBackStack(null)
//            }

        }

    }

    override fun onPause() {
        super.onPause()

        songs[nowPos].second = (songs[nowPos].playTime * binding.seekBar.progress)/1000
        songs[nowPos].isPlaying = false //스레드 중지
        setPlayerStatus(false)//정지상태일 때의 이미지로 전환

        //sharedPref : 간단한 설정값 저장에 유용함 ex 로그인시 이용
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //sharedPref 조작시 사용

        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    //앱 종료시 리소스 해제
    override fun onDestroy(){
        super.onDestroy()

        timer.interrupt() // 스레드 해제
        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.SongDao().getSongs())
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        //spf에 저장된 값과 비교해서 songlist에서
        // 해당 아이디 값을 가진 송을 찾아 songlist[nowPos]로 표현
        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())
        //스레드시작
        startTimer()
        //데이터 렌더링
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId : Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
                //이 반환 값이 nowPos값이 된다.
            }
        }
        return 0
    }

    private fun setPlayer(song: Song){
        val music = resources.getIdentifier(song.music,"raw", this.packageName)

        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songAblumIv.setImageResource(song.coverImg!!)
        binding.seekBar.progress = (song.second * 1000 / song.playTime).toInt()

        setPlayerStatus(song.isPlaying)

        if (song.isLike) {
            binding.songLikeIb.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIb.setImageResource(R.drawable.ic_my_like_off)
        }
        mediaPlayer = MediaPlayer.create(this, music)
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songPlayPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPreviousIb.setOnClickListener{
            moveSong(-1)
        }
        binding.songNextIb.setOnClickListener {
            moveSong(+1)
        }
        binding.songLikeIb.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
        binding.songRepeatIb.setOnClickListener{
            binding.songRepeatIb.visibility = View.GONE
            binding.songRepeatOn1Ib.visibility = View.VISIBLE
            Toast.makeText(applicationContext,"현재 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }
        binding.songRepeatOn1Ib.setOnClickListener{
            binding.songRepeatOn1Ib.visibility = View.GONE
            binding.songRepeatOnIb.visibility = View.VISIBLE
            Toast.makeText(applicationContext,"전체 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }
        binding.songRepeatOnIb.setOnClickListener{
            binding.songRepeatIb.visibility = View.VISIBLE
            binding.songRepeatOnIb.visibility = View.GONE
            Toast.makeText(applicationContext,"반복을 사용하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
        binding.songRandomIb.setOnClickListener {
            binding.songRandomIb.visibility = View.GONE
            binding.songRandomOnIb.visibility = View.VISIBLE
            Toast.makeText(applicationContext,"재생목록을 랜덤으로 재생합니다.", Toast.LENGTH_SHORT).show()
        }
        binding.songRandomOnIb.setOnClickListener {
            binding.songRandomIb.visibility = View.VISIBLE
            binding.songRandomOnIb.visibility = View.GONE
            Toast.makeText(applicationContext,"재생목록을 순서대로 재생합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveSong(direct:Int){
        if (nowPos + direct < 0){
            Toast.makeText(this, "첫 번째 음악입니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this, "마지막 음악입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct
        timer.interrupt()
        startTimer()

        mediaPlayer?.release() //미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.SongDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if (isLike){
            binding.songLikeIb.setImageResource(R.drawable.ic_my_like_off)
            Toast.makeText(this, "좋아요 한 곡이 취소되었습니다.",Toast.LENGTH_SHORT).show()
        }else{
            binding.songLikeIb.setImageResource(R.drawable.ic_my_like_on)
            Toast.makeText(this, "좋아요 한 곡에 담겼습니다.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        timer.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying

        if (isPlaying) {
            binding.songPlayPauseIv.visibility = View.VISIBLE
            binding.songPlayIv.visibility = View.GONE

            mediaPlayer?.start()
        } else {
            binding.songPlayPauseIv.visibility = View.GONE
            binding.songPlayIv.visibility = View.VISIBLE

            //미디어 플레이어 중지
            mediaPlayer?.pause()
        }
    }

    inner class Timer(private val playTime: Int = 0, var isPlaying: Boolean = false) : Thread() {
        private var second: Long = 0

        override fun run() {
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(1000)
                        second++

                        runOnUiThread {
                            binding.seekBar.progress =
                                (second * 1000 / playTime).toInt()
                            binding.songStartTimeTv.text = String.format(
                                "%02d:%02d",
                                TimeUnit.SECONDS.toMinutes(second),
                                second % 60
                            )
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("SONG", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }
}