package com.example.cafeapp.Profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.databinding.ActivityTentangKitaBinding

class TentangKitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTentangKitaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangKitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up back button
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}
