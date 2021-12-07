package com

import androidx.room.*

//song에 접근하기 위한 데이터 인터페이스
@Dao
interface SongDao {
    //song에 대한 어떤 동작을 제공할지 정의한다.
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable") //테이블의 모든 값을 가져와라
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    //해당 아이디값인 송만 선택해라
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id = :id")
    fun updateIsLikeById(isLike:Boolean, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx")
    fun getSongsInAlbum(albumIdx: Int): List<Song>
}