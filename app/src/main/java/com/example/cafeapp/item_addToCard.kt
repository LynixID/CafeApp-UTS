package com.example.cafeapp

data class   CartItem(
    val id: Int,  // Added unique identifier
    val name: String,
    val price: String,
    val imageResId: Int,
    var quantity: Int = 1
)