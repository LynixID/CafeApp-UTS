package com.example.cafeapp.Cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafeapp.MenuDetail.CartItem

// ViewModel untuk mengelola data keranjang belanja
class CartViewModel : ViewModel() {
    // MutableLiveData untuk menyimpan daftar item di keranjang
    private val _cartItems = MutableLiveData<MutableList<CartItem>>()
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems // LiveData untuk observasi dari UI

    // Inisialisasi dengan memuat data awal dari CartManager
    init {
        refreshItems()
    }

    // Fungsi untuk memuat ulang data item di keranjang
    fun refreshItems() {
        _cartItems.value = CartManager.getItems().toMutableList()
    }

    // Fungsi untuk menghapus semua item dari keranjang
    fun clearCart() {
        _cartItems.value = mutableListOf()
    }

    // Fungsi untuk menambahkan item ke keranjang
    fun addItem(cartItem: CartItem) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()

        // Periksa apakah item sudah ada di keranjang berdasarkan ID dan kategori
        val existingItem = currentList.find { it.id == cartItem.id && it.category == cartItem.category }

        if (existingItem != null) {
            // Jika item sudah ada, tingkatkan kuantitas
            existingItem.quantity += 1
        } else {
            // Jika item belum ada, tambahkan item baru ke daftar
            currentList.add(cartItem)
        }

        // Perbarui nilai LiveData
        _cartItems.value = currentList
    }

    // Fungsi untuk menghapus item dari keranjang berdasarkan ID
    fun removeItem(itemId: Int) {
        CartManager.removeItem(itemId) // Hapus item dari CartManager
        refreshItems() // Muat ulang data keranjang setelah penghapusan
    }
}