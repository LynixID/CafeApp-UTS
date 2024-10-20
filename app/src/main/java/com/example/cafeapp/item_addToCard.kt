package com.example.cafeapp

data class CartItem(
    val name: String,
    val price: String,
    val imagePath: String,
    var quantity: Int = 1
)
