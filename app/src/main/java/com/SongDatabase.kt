package com

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class, Album::class, User::class, Like::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun SongDao(): SongDao
    abstract fun AlbumDao(): AlbumDao
    abstract fun UserDao():UserDao

    companion object{
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"//다른 데이터 베이스랑 이름 겹치면 꼬이게 됨
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}
