package com.example.cafeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.databinding.ActivityMainBinding // Import the generated binding class
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Declare the binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflate the binding
        setContentView(binding.root) // Set the content view to the root of the binding

        // Bottom Navigation Setup
        setupBottomNavigation()

        // Load default fragment
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.nav_home // Set default selection
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
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
                    // Navigate to Cart Fragment
                    val cartFragment = CartFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, cartFragment)
                        .commit()
                    true
                }
                R.id.nav_profile -> {
                    // Navigate to Profile Fragment
                    val profileFragment = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }

        // Handle navigation from intent
        if (intent.getBooleanExtra("NAVIGATE_TO_CART", false)) {
            val cartFragment = CartFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, cartFragment)
                .commit()
            binding.bottomNavigationView.selectedItemId = R.id.cart
        }
    }
}
