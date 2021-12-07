package com

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT*FROM UserTable") //테이블의 모든 값을 가져와라
    fun getUsers(): List<User>

    @Query("SELECT*FROM UserTable WHERE email = :email AND password = :password AND  username = :username")
    fun getUser(email: String, password: String, username: String): User?

    @Query("SELECT*FROM UserTable WHERE email = :email AND password = :password")
    fun getUserinfo(email: String, password: String): User?

    @Query("SELECT*FROM UserTable WHERE username = :username")
    fun getUsername(username: Int): User?
}