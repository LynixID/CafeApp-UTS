package com.example.cafeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<MutableList<CartItem>>()
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    init {
        // Mengambil item dari CartManager
        _cartItems.value = CartManager.getItems().toMutableList()
    }

    fun addItem(item: CartItem) {
        CartManager.addItem(item)
        _cartItems.value = CartManager.getItems().toMutableList() // Update LiveData
    }

    fun removeItem(item: CartItem) {
        CartManager.removeItem(item) // Menghapus item dari CartManager
        _cartItems.value = CartManager.getItems().toMutableList() // Update LiveData
    }
}

