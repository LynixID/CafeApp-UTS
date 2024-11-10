package com.example.cafeapp.MenuDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MenuDAO {

    @Query("SELECT * FROM menus")
    fun getAll(): LiveData<List<Menu>>

    @Query("SELECT * FROM menus")
    fun getAllData(): List<Menu>

    @Insert
    fun insert(vararg menu: Menu)

    @Query("SELECT * FROM menus WHERE nama = :name")
    fun getMakanByName(name: String): LiveData<Menu>

    @Query("SELECT * FROM menus WHERE _id = :id")
    fun getMakanById(id: Int): LiveData<Menu>

    @Query("SELECT * FROM menus WHERE harga = :harga")
    fun getMakanByHex(harga: Int): LiveData<Menu>

    @Query("DELETE FROM menus WHERE _id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE menus SET nama = :name, harga = :harga, deskripsi = :deskripsi,  image_path = :namaFoto WHERE _id = :id")
    suspend fun updateMakan(id: Int, name: String, harga: Int, deskripsi: String, namaFoto: String?)
}