package com.example.cafeapp.Transaksi

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Transaksi::class], version = 1)
abstract class TransaksiDatabase  : RoomDatabase() {
    abstract fun transaksiDao(): TransaksiDAO
}