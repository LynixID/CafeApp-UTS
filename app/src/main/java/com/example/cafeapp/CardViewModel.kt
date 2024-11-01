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

    fun addItem(item: CartItem) {
        CartManager.addItem(item)
        refreshItems()
    }

    fun removeItem(itemId: Int) {
        CartManager.removeItem(itemId)
        refreshItems()
    }
}