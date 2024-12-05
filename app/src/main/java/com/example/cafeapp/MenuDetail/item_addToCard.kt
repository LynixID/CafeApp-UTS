package com.example.cafeapp.MenuDetail

import com.example.cafeapp.MenuDatabase.Kategori
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val id: Int,  // Added unique identifier
    val name: String,
    val price: String,
    val imageResId: String,
    var quantity: Int = 1,
    var category: Kategori
) : Parcelable
