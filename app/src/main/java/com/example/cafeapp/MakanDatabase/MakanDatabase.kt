package com.example.cafeapp.MakanDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "makans")
data class Makan(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    var name: String,
    var harga: Int,
    var deskripsi: String,
    @ColumnInfo(name = "image_path") val namaFoto: String
)
