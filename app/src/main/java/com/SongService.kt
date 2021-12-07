package com

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongService {
    //송에 대한 API들을 불러주는 서비스 클래스
    //AuthService와 같은 작업 방법

    //서비스 클래스와 액티비티 프래그먼트 화면과 연결해줄 뷰인터페이스 세터 만들기
    private lateinit var lookView: LookView

    //외부에서 접근해야하기 때문에 fun으로 선언
    fun setLookView(lookView: LookView){
        this.lookView = lookView
    }

    //원하는 화면에서 getSongs 호출하면 송리스트를 받아올 수 있음
    fun getSongs(){
        val SongService = getRetrofit().create(SongRetrofitInterface::class.java)

        lookView.onGetSongsLoading()

        SongService.getSongs().enqueue(object : Callback<SongResponse> {
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                Log.d("SONGSERVICE/API-RESPONSE", response.toString())

                val resp = response.body()!!

                Log.d("SONGSERVICE/API-RESPONSE-FLO", resp.toString())

                when(resp.code){
                    1000 -> lookView.onGetSongsSuccess(resp.result!!.songs)
                    else -> lookView.onGetSongsFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                //네트워크 자체가 실패했을 때
                Log.d("SONGSERVICE/API-ERROR", t.toString())

                lookView.onGetSongsFailure(400, "네트워크 오류 발생")
            }
        })
    }

    fun getRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").addConverterFactory(
            GsonConverterFactory.create()).build()
        return retrofit
    }

}