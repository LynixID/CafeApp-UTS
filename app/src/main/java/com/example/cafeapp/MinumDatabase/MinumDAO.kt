package com.example.cafeapp.MinumDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cafeapp.MakanDatabase.Makan

@Dao
interface MinumDAO {

    @Query("SELECT * FROM minums")
    fun getAll(): LiveData<List<Minum>>

    @Query("SELECT * FROM minums")
    fun getAllData(): List<Minum>
    @Insert
//    suspend fun insert(menu: Minum)
    fun insert(vararg menu: Minum)

    @Query("SELECT * FROM minums WHERE name = :name")
    fun getMinumByName(name: String): LiveData<Minum>

    @Query("SELECT * FROM minums WHERE harga = :harga")
    fun getMinumByHex(harga: Int): LiveData<Minum>

    @Query("SELECT * FROM minums WHERE _id = :id")
    fun getMinumById(id: Int): LiveData<Minum>

    @Query("DELETE FROM minums WHERE _id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE minums SET name = :name, harga = :harga, deskripsi = :deskripsi,  image_path = :namaFoto WHERE _id = :id")
    suspend fun updateMinum(id: Int, name: String, harga: Double, deskripsi: String, namaFoto: String?)
}