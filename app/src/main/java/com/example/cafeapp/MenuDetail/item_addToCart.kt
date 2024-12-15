package com.example.cafeapp.MenuDetail

import android.os.Parcelable
import com.example.cafeapp.MenuDatabase.Kategori
import kotlinx.parcelize.Parcelize

// Data class untuk item keranjang belanja, yang dapat diparse antar aktivitas
@Parcelize
data class CartItem(
    val id: Int,
    val name: String,
    val price: String,
    val imageResId: String,
    var quantity: Int = 1,
    var category: Kategori
) : Parcelable
