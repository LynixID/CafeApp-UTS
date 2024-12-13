package com.example.cafeapp.Transaksi

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Interface untuk operasi database terkait transaksi
@Dao
interface TransaksiDAO {

    // Menyisipkan data transaksi baru ke dalam database
    @Insert
    suspend fun insertTransaksi(transaksi: Transaksi)

    // Mengambil semua data transaksi dari database
    @Query("SELECT * FROM transaksi")
    suspend fun getAllTransaksi(): List<Transaksi>
}
