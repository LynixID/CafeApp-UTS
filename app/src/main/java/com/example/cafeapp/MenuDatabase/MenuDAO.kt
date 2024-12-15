package com.example.cafeapp.MenuDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// Data Access Object (DAO) untuk tabel menus
@Dao
interface MenuDAO {

    // Mengambil semua data menu dalam bentuk LiveData (reaktif)
    // Fungsi ini akan mengembalikan LiveData dari list menu, yang memungkinkan UI untuk secara otomatis
    // mendapatkan update data secara real-time ketika data berubah.
    @Query("SELECT * FROM menus")
    fun getAll(): LiveData<List<Menu>>

    // Mengambil semua data menu secara langsung (non-reaktif)
    // Fungsi ini mengembalikan daftar menu secara langsung tanpa menggunakan LiveData,
    // yang berarti data tidak akan diperbarui secara otomatis di UI.
    @Query("SELECT * FROM menus")
    fun getAllData(): List<Menu>

    // Menambahkan satu atau beberapa menu ke database
    // Fungsi ini memungkinkan untuk menambah satu atau lebih menu ke dalam tabel "menus".
    @Insert
    fun insert(vararg menu: Menu)

    // Fungsi insert untuk menambahkan satu menu, bersifat suspend untuk menghindari pemblokiran thread utama.
    @Insert
    suspend fun insert(menu: Menu): Long

    // Menghapus satu menu berdasarkan objek yang diberikan
    // Fungsi ini menghapus menu berdasarkan objek `menu` yang diberikan.
    @Delete
    suspend fun delete(menu: Menu)

    // Menghapus data menu berdasarkan ID
    // Fungsi ini menghapus menu berdasarkan ID tertentu.
    @Query("DELETE FROM menus WHERE _id = :menuId")
    suspend fun deleteById(menuId: Int)

    // Mengambil data menu berdasarkan nama dalam bentuk LiveData
    // Fungsi ini mengembalikan LiveData dari menu berdasarkan nama. Fungsi ini berguna ketika kita ingin
    // mendapatkan data menu berdasarkan nama dan ingin menggunakan LiveData untuk update otomatis.
    @Query("SELECT * FROM menus WHERE nama = :name")
    fun getMakanByName(name: String): LiveData<Menu>

    // Mengambil data menu berdasarkan ID dalam bentuk LiveData
    // Fungsi ini mengembalikan LiveData dari menu berdasarkan ID. Sama seperti fungsi getMakanByName, namun berdasarkan ID.
    @Query("SELECT * FROM menus WHERE _id = :id")
    fun getMakanById(id: Int): LiveData<Menu>

    // Mengambil data menu berdasarkan harga dalam bentuk LiveData
    // Fungsi ini mengembalikan LiveData dari menu berdasarkan harga.
    @Query("SELECT * FROM menus WHERE harga = :harga")
    fun getMakanByHex(harga: Int): LiveData<Menu>

    // Memperbarui data menu berdasarkan ID
    // Fungsi ini memungkinkan untuk memperbarui data menu tertentu berdasarkan ID,
    // dengan memperbarui nama, harga, deskripsi, dan foto menu.
    @Query("UPDATE menus SET nama = :name, harga = :harga, deskripsi = :deskripsi,  image_path = :namaFoto WHERE _id = :id")
    suspend fun updateMakan(id: Int, name: String, harga: Int, deskripsi: String, namaFoto: String?)
}