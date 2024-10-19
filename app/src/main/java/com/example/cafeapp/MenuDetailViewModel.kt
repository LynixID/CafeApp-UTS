package com.example.cafeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuDetailViewModel : ViewModel() {

    // MutableLiveData untuk menyimpan data menu
    private val _menuName = MutableLiveData<String>()
    val menuName: LiveData<String> = _menuName

    private val _menuPrice = MutableLiveData<String>()
    val menuPrice: LiveData<String> = _menuPrice

    private val _menuDescription = MutableLiveData<String>()
    val menuDescription: LiveData<String> = _menuDescription

    private val _menuImageResId = MutableLiveData<Int>()
    val menuImageResId: LiveData<Int> = _menuImageResId

    // Fungsi untuk mengupdate data di ViewModel
    fun setMenuDetails(name: String, price: String, description: String, imageResId: Int) {
        _menuName.value = name
        _menuPrice.value = price
        _menuDescription.value = description
        _menuImageResId.value = imageResId
    }
}
