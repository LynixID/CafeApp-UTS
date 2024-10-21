package com.example.cafeapp

object CartManager {
    private val cartItems = mutableMapOf<Int, CartItem>()

    fun getItems(): List<CartItem> = cartItems.values.toList()

    fun addItem(item: CartItem) {
        val existingItem = cartItems[item.id]
        if (existingItem != null) {
            // Update quantity of existing item
            cartItems[item.id] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // Add new item
            cartItems[item.id] = item
        }
    }

    fun updateItemQuantity(itemId: Int, quantity: Int) {
        cartItems[itemId]?.let { item ->
            if (quantity <= 0) {
                cartItems.remove(itemId)
            } else {
                cartItems[itemId] = item.copy(quantity = quantity)
            }
        }
    }

    fun removeItem(itemId: Int) {
        cartItems.remove(itemId)
    }

    fun clear() {
        cartItems.clear()
    }
}
