package com.example.cafeapp.Cart

import com.example.cafeapp.MenuDetail.CartItem

// Manajer Keranjang Belanja
object CartManager {
    // Menyimpan item keranjang dalam bentuk map dengan ID sebagai kunci
    private val cartItems = mutableMapOf<String, CartItem>()

    // Mendapatkan daftar semua item di keranjang
    fun getItems(): List<CartItem> = cartItems.values.toList()

    // Menambahkan item ke keranjang
    fun addItem(item: CartItem) {
        val itemIdString = item.id.toString() // Menggunakan ID item sebagai kunci
        val existingItem = cartItems[itemIdString]

        if (existingItem != null) {
            // Jika item sudah ada, perbarui kuantitas
            cartItems[itemIdString] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // Jika item belum ada, tambahkan sebagai item baru
            cartItems[itemIdString] = item
        }
    }

    // Menghapus item dari keranjang berdasarkan ID
    fun removeItem(itemId: Int) {
        cartItems.remove(itemId.toString()) // Menghapus berdasarkan kunci ID
    }

    // Menghapus semua item dari keranjang
    fun clear() {
        cartItems.clear()
    }
}