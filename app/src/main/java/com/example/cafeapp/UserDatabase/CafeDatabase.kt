package com.example.cafeapp.UserDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cafeapp.MenuDatabase.KategoriConverter
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuDAO
import com.example.cafeapp.Transaksi.Transaksi
import com.example.cafeapp.Transaksi.TransaksiDAO

// Database untuk aplikasi kafe yang mencakup Menu, User, dan Transaksi
@Database(entities = [Menu::class, User::class, Transaksi::class], version = 12, exportSchema = false)
@TypeConverters(KategoriConverter::class) // Menambahkan konverter untuk jenis data Kategori
abstract class CafeDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDAO // Mendeklarasikan DAO untuk Menu
    abstract fun userDao(): UserDao // Mendeklarasikan DAO untuk User
    abstract fun transaksiDao(): TransaksiDAO // Mendeklarasikan DAO untuk Transaksi

    companion object {
        @Volatile
        private var INSTANCE: CafeDatabase? = null

        // Fungsi untuk mendapatkan instance dari CafeDatabase (singleton)
        fun getInstance(context: Context): CafeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CafeDatabase::class.java,
                    "cafe_database"
                )
                    .fallbackToDestructiveMigration() // Jika terjadi perubahan pada schema, akan menghapus dan membuat ulang database
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
