package com.example.cafeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<MutableList<CartItem>>()
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    init {
        refreshItems()
    }

    fun refreshItems() {
        _cartItems.value = CartManager.getItems().toMutableList()
    }

    fun addItem(cartItem: CartItem) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()

        // Cek jika item sudah ada
        val existingItem = currentList.find { it.id == cartItem.id && it.category == cartItem.category }

        if (existingItem != null) {
            // Jika ada, tingkatkan kuantitas
            existingItem.quantity += 1
        } else {
            // Jika tidak ada, tambahkan item baru
            currentList.add(cartItem)
        }

        _cartItems.value = currentList
    }

    fun removeItem(itemId: Int, category: String) {
        CartManager.removeItem(itemId, category)
        refreshItems()
    }
}