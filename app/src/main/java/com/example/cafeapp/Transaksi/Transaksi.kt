package com.example.cafeapp.Transaksi

import androidx.room.Entity
import androidx.room.PrimaryKey

// Menentukan entitas Transaksi yang akan disimpan dalam database Room
@Entity
data class Transaksi(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val namaMenu: String,
    val jumlahTransaksi: Int,
    val pembayaran: Int,
    val kembalian: Int,
    val waktu: String,
    val imagePath: String?
)

