package com.example.cafeapp

import android.util.Log

object CartManager {
    private val cartItems = mutableMapOf<String, CartItem>()

    fun getItems(): List<CartItem> = cartItems.values.toList()

    fun addItem(item: CartItem) {
        Log.d("ITEM MASUK", item.toString())
        var prefixString = ""
        if(item.category == "Makan"){
            prefixString = "F"
        }else{
            prefixString = "D"

        }
        val existingItem = cartItems[prefixString+item.id.toString()]
        if (existingItem != null) {
            // Update quantity of existing item
            cartItems.forEach { (id, cartItem) ->

                if (cartItem.name == item.name) {
                    cartItems[prefixString+item.id.toString()] = existingItem.copy(quantity = existingItem.quantity + 1)
                }else{
                    cartItems[prefixString+item.id.toString()] = item
                }

            }
        } else {
            // Add new item
            cartItems[prefixString+item.id.toString()] = item
        }
    }

//    fun updateItemQuantity(itemId: Int, quantity: Int) {
//        cartItems[itemId]?.let { item ->
//            if (quantity <= 0) {
//                cartItems.remove(itemId)
//            } else {
//                cartItems[itemId] = item.copy(quantity = quantity)
//            }
//        }
//    }

    fun removeItem(itemId: Int, category: String) {
        var prefixString = ""
        if(category == "Makan"){
            prefixString = "F"
        }else{
            prefixString = "D"

        }
        cartItems.remove(prefixString+itemId.toString())
    }

    fun clear() {
        cartItems.clear()
    }
}
