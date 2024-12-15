package com.example.cafeapp.UserDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// DAO (Data Access Object) untuk operasi database pada entitas User
@Dao
interface UserDao {
    // Menyisipkan pengguna baru ke dalam database
    @Insert
    fun insertTest(vararg user: User) // Menggunakan vararg untuk menyisipkan beberapa pengguna sekaligus

    // Menyisipkan pengguna baru secara suspend (dapat dijalankan dalam coroutine)
    @Insert
    suspend fun insert(user: User)

    // Memperbarui data pengguna yang ada
    @Update
    suspend fun update(user: User)

    // Menghapus pengguna dari database
    @Delete
    suspend fun delete(user: User)

    // Mengambil semua pengguna dari database
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User> // Menggunakan coroutine untuk menjalankan query ini di background thread

    // Mengambil semua pengguna dari database (metode untuk test)
    @Query("SELECT * FROM users")
    fun getAllUsersTest(): List<User> // Mengembalikan daftar semua pengguna

    // Mengambil pengguna berdasarkan username dan password
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUserByUsernameAndPassword(username: String, password: String): User?
}