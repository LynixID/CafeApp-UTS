package com.example.cafeapp.Cart

import com.example.cafeapp.MenuDetail.CartItem

object CartManager {
    private val cartItems = mutableMapOf<String, CartItem>()

    fun getItems(): List<CartItem> = cartItems.values.toList()

    fun addItem(item: CartItem) {
        // Use the item's ID directly for the key
        val itemIdString = item.id.toString()

        val existingItem = cartItems[itemIdString]
        if (existingItem != null) {
            // Update quantity of existing item
            cartItems[itemIdString] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // Add new item
            cartItems[itemIdString] = item
        }
    }

    fun removeItem(itemId: Int) {
        // Directly remove based on item ID
        cartItems.remove(itemId.toString())
    }

    fun clear() {
        cartItems.clear()
    }
}
