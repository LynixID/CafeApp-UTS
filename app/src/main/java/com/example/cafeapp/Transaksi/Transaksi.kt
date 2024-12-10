package com.example.cafeapp.Transaksi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaksi(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val namaMenu: String,  // This will contain the itemized list
    val jumlahTransaksi: Int,
    val pembayaran: Int,
    val kembalian: Int,
    val waktu: String,
    val imagePath: String?
)

