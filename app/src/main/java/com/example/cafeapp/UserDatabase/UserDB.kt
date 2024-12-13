package com.example.cafeapp.UserDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entitas untuk menyimpan data pengguna di dalam tabel "user"
@Entity(tableName = "user")
data class UserDB(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID lokal untuk Room
    val key: String = "", // Kunci unik dari Firebase
    val password: String = "",
    val username: String = "",
    val role: String = ""// "staff" atau "admin"
)
