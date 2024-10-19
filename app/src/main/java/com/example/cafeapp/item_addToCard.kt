package com.example.cafeapp

data class item_addToCard(
    val imageResId: String,
    val name: String,
    val price: String,
    var quantity: Int)