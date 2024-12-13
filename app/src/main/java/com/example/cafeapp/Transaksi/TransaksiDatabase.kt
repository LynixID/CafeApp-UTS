package com.example.cafeapp.Transaksi

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Database untuk transaksi, menggunakan Room sebagai ORM (Object Relational Mapping)
@Database(entities = [Transaksi::class], version = 11)
abstract class TransaksiDatabase : RoomDatabase() {
    abstract fun transaksiDao(): TransaksiDAO // Mendeklarasikan DAO untuk transaksi

    companion object {
        @Volatile
        private var INSTANCE: TransaksiDatabase? = null

        // Fungsi untuk mendapatkan instance dari TransaksiDatabase (singleton)
        fun getInstance(context: Context): TransaksiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransaksiDatabase::class.java,
                    "transaksi_database"
                )
                    .fallbackToDestructiveMigration() // Menambahkan fallback untuk migrasi yang gagal
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
