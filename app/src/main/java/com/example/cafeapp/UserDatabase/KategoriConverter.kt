package com.example.cafeapp.MenuDatabase

import androidx.room.TypeConverter

// Konverter untuk mengonversi antara tipe data Kategori dan String
class KategoriConverter {
    // Mengonversi objek Kategori menjadi String
    @TypeConverter
    fun fromKategori(kategori: Kategori): String {
        return kategori.name
    }

    // Mengonversi String menjadi objek Kategori
    @TypeConverter
    fun toKategori(value: String): Kategori {
        return Kategori.valueOf(value)
    }
}