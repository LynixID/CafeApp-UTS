package com.example.cafeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.databinding.ActivityMainBinding

    class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding
        private lateinit var viewModel: MainViewModel

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.main_menu, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_logout -> {
                    // Implement logout logic here
                    finish() // This will close the app
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

            binding.lifecycleOwner = this
            binding.viewModel = viewModel

            setupNavigation()
        }

        private fun setupNavigation() {
            binding.bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> loadFragment(HomeFragment())
                    R.id.nav_about -> loadFragment(AboutUsFragment())
                    R.id.nav_app_info -> loadFragment(AppInfoFragment())
                    R.id.nav_help -> loadFragment(HelpFragment())
                }
                true
            }
        }

        private fun loadFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }