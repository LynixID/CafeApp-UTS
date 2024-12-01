package com.example.cafeapp.Home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dapatkan NavController dari NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Integrasi BottomNavigationView dengan NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        // Tangani navigasi khusus jika intent mengarah ke CartFragment
        if (intent.getBooleanExtra("NAVIGATE_TO_CART", false)) {
            navController.navigate(R.id.cartFragment)
        }
    }
}
