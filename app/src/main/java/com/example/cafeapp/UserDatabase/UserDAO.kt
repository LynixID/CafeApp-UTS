package com.example.cafeapp.UserDatabase

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insertTest(vararg user :User)

    @Insert
    suspend fun insert(user :User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User> // Pastikan ini ada di dalam coroutine

    @Query("SELECT * FROM users") // Tambahkan ini untuk mengambil semua pengguna
    fun getAllUsersTest(): List<User> // Mengembalikan daftar semua user

//    @Query("SELECT * FROM users WHERE id IN (:userIds)")
//    suspend fun getUsersByIds(vararg userIds: Int): List<User>


    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUserByUsernameAndPassword(username: String, password: String): User?
}



