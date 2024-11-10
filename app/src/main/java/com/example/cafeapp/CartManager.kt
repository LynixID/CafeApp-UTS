package com.example.cafeapp

import android.util.Log
import com.example.cafeapp.MenuDatabase.Kategori

object CartManager {
    private val cartItems = mutableMapOf<String, CartItem>()

    fun getItems(): List<CartItem> = cartItems.values.toList()

    fun addItem(item: CartItem) {
        // Use the enum value's string representation for the prefix
        val prefixString = when (item.category) {
            Kategori.MAKAN -> "F"
            Kategori.MINUM -> "D"
        }

        val existingItem = cartItems[prefixString + item.id.toString()]
        if (existingItem != null) {
            // Update quantity of existing item
            cartItems[prefixString + item.id.toString()] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // Add new item
            cartItems[prefixString + item.id.toString()] = item
        }
    }

    fun removeItem(itemId: Int, category: Kategori) {
        // Use the enum to get the correct prefix
        val prefixString = when (category) {
            Kategori.MAKAN -> "F"
            Kategori.MINUM -> "D"
        }
        cartItems.remove(prefixString + itemId.toString())
    }

    fun clear() {
        cartItems.clear()
    }
}