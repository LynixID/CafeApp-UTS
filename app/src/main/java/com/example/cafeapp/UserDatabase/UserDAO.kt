package com.example.cafeapp.UserDatabase

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users") // Tambahkan ini untuk mengambil semua pengguna
    suspend fun getAllUsers(): List<User> // Mengembalikan daftar semua user

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUserByUsernameAndPassword(username: String, password: String): User?
}



