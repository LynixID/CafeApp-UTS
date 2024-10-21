package com.example.cafeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class BantuanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bantuan)

        // Tombol WhatsApp
        val cardViewWA = findViewById<CardView>(R.id.cardViewWA)
        cardViewWA.setOnClickListener {
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
        val cardViewIG = findViewById<CardView>(R.id.cardViewIG)
        cardViewIG.setOnClickListener {
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
        val backButton: ImageButton = findViewById(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }
    }
}