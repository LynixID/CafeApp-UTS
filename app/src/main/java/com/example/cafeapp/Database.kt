package com.example.cafeapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "makans")
data class Makan(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val name: String,
    val harga: Int,
    @ColumnInfo(name = "image_path") val namaFoto: String
)

@Entity(tableName = "minums")
data class Minum(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val name: String,
    val harga: Int,
    @ColumnInfo(name = "image_path") val namaFoto: String

)

@Entity(tableName = "snacks")
data class Snack(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val name: String,
    val harga: Int,
    @ColumnInfo(name = "image_path") val namaFoto: String

)