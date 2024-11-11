package com.example.cafeapp

import com.example.cafeapp.MenuDatabase.Kategori

data class CartItem(
    val id: Int,  // Added unique identifier
    val name: String,
    val price: String,
    val imageResId: String,
    var quantity: Int = 1,
    var category: Kategori
)