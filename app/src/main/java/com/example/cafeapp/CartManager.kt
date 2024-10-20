package com.example.cafeapp

import android.util.Log

object CartManager {
    private val cartItems: MutableList<CartItem> = mutableListOf()

    fun getItems(): List<CartItem> = cartItems

    fun addItem(item: CartItem) {
        cartItems.add(item)
        Log.d("CartManager", "Item added: $item") // Tambahkan log ini
    }
    fun removeItem(item: CartItem) {
        cartItems.remove(item)
    }
}

