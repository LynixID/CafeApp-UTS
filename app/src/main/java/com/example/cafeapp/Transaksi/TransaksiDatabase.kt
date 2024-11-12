package com.example.cafeapp.Transaksi

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaksi::class], version = 1)
abstract class TransaksiDatabase : RoomDatabase() {
    abstract fun transaksiDao(): TransaksiDAO

    companion object {
        @Volatile
        private var INSTANCE: TransaksiDatabase? = null

        fun getInstance(context: Context): TransaksiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransaksiDatabase::class.java,
                    "transaksi_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}