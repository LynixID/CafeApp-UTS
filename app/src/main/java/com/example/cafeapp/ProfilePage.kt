package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfilePage : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupListeners()
    }

    // Setting up the UI for the profile page
    private fun setupUI() {
        binding.nameTextView.text = "Robert Martin"
        binding.profileImageView.setImageResource(R.drawable.profile_image)
    }

    // Setting up listeners for the buttons and navigation
    private fun setupListeners() {
        binding.aboutUsButton.setOnClickListener {
            // Handle Tentang Kita click
            // You might want to navigate to another activity or fragment that displays the team members
        }

        binding.aboutAppButton.setOnClickListener {
            // Handle Tentang Aplikasi click
            // Show information about the application, likely navigating to another screen
        }

        binding.helpButton.setOnClickListener {
            // Handle Bantuan click
            // Navigate to a Help section or open external social media links (e.g., Instagram, WhatsApp)
        }

        binding.logoutButton.setOnClickListener {
            // Implement logout logic
            finishAffinity() // Closes all activities and exits the app
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle navigation to Home
                    // Navigate to the HomeFragment or HomeActivity
                    true
                }
                R.id.nav_search -> {
                    // Handle navigation to Search
                    true
                }
                R.id.nav_cart -> {
                    // Handle navigation to Cart
                    true
                }
                R.id.nav_profile -> {
                    // Already on profile, do nothing
                    true
                }
                else -> false
            }
        }
    }
}
