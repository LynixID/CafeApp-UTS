package com.example.cafeapp.Transaksi

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "transaksi")
data class Transaksi(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val namaMenu: String,
    val pembayaran: Int,
    val jumlahTransaksi: Int
)
