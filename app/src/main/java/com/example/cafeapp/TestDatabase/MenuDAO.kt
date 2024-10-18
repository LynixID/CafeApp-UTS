package com.example.cafeapp.TestDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MenuDAO {

    @Query("SELECT * FROM colors")
    fun getAll(): LiveData<List<Menu>>

    @Insert
    suspend fun insert(color: Menu)


    @Query("SELECT * FROM colors WHERE name = :name")
    fun getMenuByName(name: String): LiveData<Menu>

    @Query("SELECT * FROM colors WHERE harga = :harga")
    fun getMenuByHex(harga: Int): LiveData<Menu>

    @Query("DELETE FROM colors WHERE _id = :id")
    suspend fun deleteById(id: Int)
}