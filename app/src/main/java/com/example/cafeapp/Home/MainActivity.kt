package com.example.cafeapp.Home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup bottom navigation with NavController
        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener { menuItem ->
                try {
                    navController.navigate(menuItem.itemId)
                    true
                } catch (e: Exception) {
                    false
                }
            }
        }

        // Handle intent extra for cart navigation
        if (intent.getBooleanExtra("NAVIGATE_TO_CART", false)) {
            navController.navigate(R.id.cartFragment)
        }
    }
}