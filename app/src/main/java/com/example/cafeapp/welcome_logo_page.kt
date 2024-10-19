package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class welcome_logo_page : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000 // 3 detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_logo)

        Handler().postDelayed({
            val intent = Intent(this@welcome_logo_page, LoginPage::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}