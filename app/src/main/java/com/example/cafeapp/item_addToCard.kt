package com.example.cafeapp

data class CartItem(
    val name: String,
    val price: String,
    val imageResId: Int,
    var quantity: Int // Jumlah item
)
