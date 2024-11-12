package com.example.cafeapp.Transaksi

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransaksiDAO {
    @Insert
    suspend fun insert(transaksi: Transaksi)

    @Query("SELECT * FROM transaksi")
    suspend fun getAllTransaksi(): List<Transaksi>
}