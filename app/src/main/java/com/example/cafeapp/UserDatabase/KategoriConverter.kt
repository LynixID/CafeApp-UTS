package com.example.cafeapp.MenuDatabase

import androidx.room.TypeConverter

class KategoriConverter {
    @TypeConverter
    fun fromKategori(kategori: Kategori): String {
        return kategori.name
    }

    @TypeConverter
    fun toKategori(value: String): Kategori {
        return Kategori.valueOf(value)
    }
}
