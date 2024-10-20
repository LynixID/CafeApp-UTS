package com.example.cafeapp.MinumDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "minums")
data class Minum(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val name: String,
    val harga: Int,
    val deskripsi: String,
    @ColumnInfo(name = "image_path") val namaFoto: String

)
