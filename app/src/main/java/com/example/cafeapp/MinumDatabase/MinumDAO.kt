package com.example.cafeapp.MinumDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MinumDAO {

    @Query("SELECT * FROM minums")
    fun getAll(): LiveData<List<Minum>>

    @Insert
    suspend fun insert(menu: Minum)

    @Query("SELECT * FROM minums WHERE name = :name")
    fun getMinumByName(name: String): LiveData<Minum>

    @Query("SELECT * FROM minums WHERE harga = :harga")
    fun getMinumByHex(harga: Int): LiveData<Minum>

    @Query("DELETE FROM minums WHERE _id = :id")
    suspend fun deleteById(id: Int)
}