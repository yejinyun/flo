package com

import androidx.room.*


@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    //like 테이블 사용
    @Insert
    fun likeAlbum(like: Like)

    //like가 된건지 안 된건지 확인
    //라이크 테이블의 아이디를 반환
    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikeAlbum(userId: Int, albumId: Int): Int?

    //좋아요가 안 된건지 확인
    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikeAlbum(userId: Int, albumId: Int): Int?

    //liketable을 기준으로 albumtable을 join한다. id값이 같은 것끼리..
    //LT는 liketable의 줄임말
    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")
    fun getLikeAlbum(userId: Int): List<Album>

    //isLike값을 바꾼다.
    @Query("UPDATE AlbumTable SET isLike = :isLike WHERE id = :id")
    fun updateIsLikeById(isLike:Boolean, id: Int)
}