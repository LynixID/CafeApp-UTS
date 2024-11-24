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
                R.id.nav_transaction -> {
                    // Navigate to Transaction Fragment
                    val transaksiFragment = TransaksiFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, transaksiFragment)
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

        // Check if we need to navigate to the CartFragment
        if (intent.getBooleanExtra("NAVIGATE_TO_CART", false)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CartFragment()) // Replace with CartFragment
                .commit()
        }
    }
}
