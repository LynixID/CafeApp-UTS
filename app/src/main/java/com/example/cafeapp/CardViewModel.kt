package com.example.cafeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafeapp.item_addToCard

class CardViewModel : ViewModel() {

    // MutableLiveData untuk menyimpan list item keranjang
    private val _cartItems = MutableLiveData<MutableList<item_addToCard>>()
    val cartItems: LiveData<MutableList<item_addToCard>> = _cartItems

    init {
        // Contoh data awal
        _cartItems.value = mutableListOf(
            item_addToCard(R.drawable.ic_launcher_background.toString(), "Item 1", "Rp 10.000", 1),
            item_addToCard(R.drawable.ic_launcher_background.toString(), "Item 2", "Rp 20.000", 2),
            item_addToCard(R.drawable.ic_launcher_background.toString(), "Item 3", "Rp 30.000", 1)
        )
    }

    // Fungsi untuk menambah item ke dalam keranjang
    fun addItem(item: item_addToCard) {
        _cartItems.value?.add(item)
        _cartItems.value = _cartItems.value // Trigger observer
    }

    // Fungsi untuk menghapus item dari keranjang
    fun removeItem(item: item_addToCard) {
        _cartItems.value?.remove(item)
        _cartItems.value = _cartItems.value // Trigger observer
    }
}
