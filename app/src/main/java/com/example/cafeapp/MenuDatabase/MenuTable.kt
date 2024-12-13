package com.example.cafeapp.MenuDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Entitas untuk tabel "menus" di database Room
@Entity(tableName = "menus")
data class Menu(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    var nama: String = "",  // Default value
    var harga: Int = 0,     // Default value
    var deskripsi: String = "",  // Default value
    var kategori: Kategori = Kategori.MAKAN,  // Default value
    @ColumnInfo(name = "image_path") val namaFoto: String = ""  // Default value
)

// Enum untuk kategori menu
enum class Kategori {
    MAKAN,
    MINUM
}