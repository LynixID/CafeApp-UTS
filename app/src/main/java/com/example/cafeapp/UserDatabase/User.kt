package com.example.cafeapp.UserDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entitas untuk menyimpan data pengguna di dalam tabel "users"
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String,
    val role: String // "staff" or "admin"
)