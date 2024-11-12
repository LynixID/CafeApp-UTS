package com.example.cafeapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.MenuDatabase.MenuDAO
import com.example.cafeapp.Transaksi.Transaksi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransaksiActivity : AppCompatActivity() {

    private lateinit var db: CafeDatabase
    private lateinit var menuDao: MenuDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaksi_activity)

        // Inisialisasi CafeDatabase dan MenuDAO
        db = CafeDatabase.getInstance(this)
        menuDao = db.menuDao()

        val menuId = 1
        val jumlahTransaksi = 2

        // Mengambil data menu dari database
        lifecycleScope.launch(Dispatchers.IO) {
            // Menggunakan getMakanById yang mengembalikan LiveData<Menu>
            val menu = menuDao.getMakanById(menuId).value

            if (menu != null) {
                val totalHarga = menu.harga * jumlahTransaksi
                val tanggalTransaksi = getCurrentDate()
                val jamTransaksi = getCurrentTime()

                // Membuat objek transaksi
                val transaksi = Transaksi(
                    id = 0,
                    namaMenu = menu.nama,
                    harga = totalHarga,
                    jumlahTransaksi = jumlahTransaksi,
                    tanggalTransaksi = tanggalTransaksi,
                    jamTransaksi = jamTransaksi
                )

                // Menyimpan transaksi ke dalam database
                val transaksiDao = db.transaksiDao()
                transaksiDao.insert(transaksi)

                launch(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Transaksi berhasil", Toast.LENGTH_SHORT).show()
                }
            } else {
                launch(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Menu tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(Date())
    }
}
