package com

import retrofit2.Call
import retrofit2.http.GET

//API 인터페이스
interface SongRetrofitInterface {
    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}