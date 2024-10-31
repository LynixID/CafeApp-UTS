package com.example.cafeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.databinding.ActivityTentangAplikasiBinding

class TentangAplikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTentangAplikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangAplikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}
