package com.example.cafeapp.MakanDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MakanDAO {

    @Query("SELECT * FROM makans")
    fun getAll(): LiveData<List<Makan>>

    @Query("SELECT * FROM makans WHERE _id = :id LIMIT 1")
    fun getMakanById(id: Int): LiveData<Makan>

    @Insert
    suspend fun insert(color: Makan)

    @Query("SELECT * FROM makans WHERE name = :name")
    fun getMakanByName(name: String): LiveData<Makan>

    @Query("SELECT * FROM makans WHERE harga = :harga")
    fun getMakanByHex(harga: Int): LiveData<Makan>

    @Query("DELETE FROM makans WHERE _id = :id")
    suspend fun deleteById(id: Int)
}