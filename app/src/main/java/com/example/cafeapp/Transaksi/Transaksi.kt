package com.example.cafeapp.Transaksi

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "transaksi")
data class Transaksi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaMenu: String,
    val harga: Int,
    val jumlahTransaksi: Int,
    val tanggalTransaksi: String,
    val jamTransaksi: String
)

