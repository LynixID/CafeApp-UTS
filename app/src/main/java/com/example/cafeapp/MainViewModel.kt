package com.example.cafeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _currentScreen = MutableLiveData<Screen>()
    val currentScreen: LiveData<Screen> = _currentScreen

    fun setScreen(screen: Screen) {
        _currentScreen.value = screen
    }
}

enum class Screen {
    HOME, ABOUT_US, APP_INFO, HELP
}