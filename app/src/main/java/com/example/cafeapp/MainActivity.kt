package com.example.cafeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Bottom Navigation Setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Navigate to Home Fragment
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .commit()
                    true
                }
                R.id.nav_favorites -> {
                    // Navigate to Favorites Fragment
                    val favoritesFragment = FavoritesFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, favoritesFragment)
                        .commit()
                    true
                }
                R.id.cart -> {
                    val cartFragment = CartFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, cartFragment)
                        .commit()
                    true
                }
                R.id.nav_profile -> {
                    val profileFragment = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }

        if (intent.getBooleanExtra("NAVIGATE_TO_CART", false)) {
            val cartFragment = CartFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, cartFragment)
                .commit()
            bottomNavigationView.selectedItemId = R.id.cart
        }

        // Load default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home // Set default selection
        }
    }
}