package com.example.cafeapp.Home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityMainBinding

// Aktivitas utama aplikasi
class MainActivity : AppCompatActivity() {
    // Deklarasi variabel binding dan NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    // Fungsi utama yang dijalankan saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inisialisasi binding
        setContentView(binding.root)

        // Setup Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment // Temukan NavHostFragment
        navController = navHostFragment.navController // Inisialisasi NavController

        // Setup Bottom Navigation dengan NavController
        binding.bottomNavigationView.apply {
            setupWithNavController(navController) // Hubungkan BottomNavigationView dengan NavController
            setOnItemSelectedListener { menuItem -> // Listener untuk pemilihan item di BottomNavigationView
                try {
                    navController.navigate(menuItem.itemId) // Navigasi ke item yang dipilih
                    true
                } catch (e: Exception) {
                    false // Jika navigasi gagal
                }
            }
        }

        // Handle intent untuk navigasi langsung ke keranjang
        if (intent.getBooleanExtra("NAVIGATE_TO_CART", false)) {
            navController.navigate(R.id.cartFragment) // Navigasi ke fragment keranjang
        }
    }
}