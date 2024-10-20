package com.example.cafeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    init {
        // Mengambil item dari CartManager
        _cartItems.value = CartManager.getItems().toMutableList()
    }

    fun addItem(item: CartItem) {
        val currentItems = _cartItems.value ?: mutableListOf()
        val existingItem = currentItems.find { it.name == item.name }

        if (existingItem != null) {
            existingItem.quantity += item.quantity // Tambah kuantitas jika item sudah ada
        } else {
            currentItems.add(item) // Tambah item baru jika belum ada
        }

        _cartItems.value = currentItems // Update LiveData
        Log.d("CardViewModel", "Items in cart: $currentItems") // Tambahkan log untuk melihat isi keranjang
    }

    fun removeItem(item: CartItem) {
        val currentItems = _cartItems.value ?: mutableListOf()
        currentItems.remove(item) // Hapus item
        _cartItems.value = currentItems // Update LiveData
    }
}
