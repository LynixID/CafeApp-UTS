package com.example.cafeapp.MakanDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "makans")
data class Makan(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val name: String,
    val harga: Int,
    @ColumnInfo(name = "image_path") val namaFoto: String
)
