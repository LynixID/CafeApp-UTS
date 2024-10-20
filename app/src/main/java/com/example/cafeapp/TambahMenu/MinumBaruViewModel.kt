package com.example.cafeapp.TambahMenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafeapp.R
import com.example.cafeapp.item_menuBaru

class MinumBaruViewModel : ViewModel() {

    private val _menuList = MutableLiveData<List<item_menuBaru>?>()
    val menuList: MutableLiveData<List<item_menuBaru>?> get() = _menuList

    init {
        _menuList.value = listOf(
            item_menuBaru("Menu 1", "Rp 10.000", R.drawable.ic_launcher_background, "enakkk"),
            item_menuBaru("Menu 2", "Rp 20.000", R.drawable.ic_launcher_background, "enakk jugaa"),
            item_menuBaru("Menu 3", "Rp 30.000", R.drawable.ic_launcher_background, "enakk juga lagi"),
            item_menuBaru("Menu 4", "Rp 40.000", R.drawable.ic_launcher_background, "enakk lagi")
        )
    }

    fun deleteItem(item: item_menuBaru) {
        val currentList = _menuList.value?.toMutableList()
        currentList?.remove(item)
        _menuList.value = currentList
    }
}
