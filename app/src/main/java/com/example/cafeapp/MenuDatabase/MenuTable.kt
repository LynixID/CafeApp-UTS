package com.example.cafeapp.MenuDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menus")
data class Menu(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    var nama: String,
    var harga: Int,
    var deskripsi: String,
    var kategori: Kategori,
    @ColumnInfo(name = "image_path") val namaFoto: String
)

enum class Kategori {
    MAKAN, MINUM
}
