package com.example.cafeapp.TestDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "colors")
data class Menu(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val name: String,
    @ColumnInfo(name = "hex_color") val hex: String,
    @ColumnInfo(name = "image_path") val imagePath: String
)