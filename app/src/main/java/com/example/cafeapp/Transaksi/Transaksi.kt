package com.example.cafeapp.Transaksi

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "transaksi")
data class Transaksi(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val namaMenu: String,
    val harga: Int,
    val pembayaran: Int,
    val kembalian: Int,
    val tanggalTransaksi: String,
    val jamTransaksi: String,
    val jumlahTransaksi: Int
)
