package com.example.cafeapp.Profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.databinding.ActivityBantuanBinding

class BantuanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBantuanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBantuanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol WhatsApp
        binding.cardViewWA.setOnClickListener {
            val number = "6283845586939"
            val url = "https://api.whatsapp.com/send?phone=$number"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "WhatsApp tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Instagram
        binding.cardViewIG.setOnClickListener {
            val instagramUrl = "https://www.instagram.com/inftechd"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(instagramUrl)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Instagram tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Back
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}
