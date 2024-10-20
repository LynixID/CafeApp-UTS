package com.example.cafeapp

object CartManager {
    private val cartItems: MutableList<CartItem> = mutableListOf()

    fun getItems(): List<CartItem> = cartItems

    fun addItem(item: CartItem) {
        cartItems.add(item)
    }

    fun removeItem(item: CartItem) {
        cartItems.remove(item)
    }
}

