package com

interface LookView {
    //액티비티에서 작업을 처리할 수 있는 뷰인터페이스 만들기
    fun onGetSongsLoading()
    fun onGetSongsSuccess(songs: ArrayList<Song>)
    fun onGetSongsFailure(code: Int, message: String)
}