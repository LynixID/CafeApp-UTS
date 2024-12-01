package com.example.cafeapp.Transaksi

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity
data class Transaksi(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val namaMenu: String,
    val jumlahTransaksi: Int,
    val pembayaran: Int,
    val waktu: String
)

