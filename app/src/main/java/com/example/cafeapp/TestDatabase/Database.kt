package com.example.cafeapp.TestDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "colors")
data class Menu(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val name: String,
    val harga: Int,
    @ColumnInfo(name = "image_path") val imagePath: String
)