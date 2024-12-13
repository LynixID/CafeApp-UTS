package com.example.cafeapp.Login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.R

// Aktivitas untuk halaman splash screen dengan logo selamat datang
class welcome_logo_page : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000 // Durasi splash screen dalam milidetik (3 detik)

    // Fungsi utama saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_logo) // Mengatur layout untuk splash screen

        // Handler untuk menjalankan transisi setelah SPLASH_TIME_OUT
        Handler().postDelayed({
            // Navigasi ke halaman login setelah durasi selesai
            val intent = Intent(this@welcome_logo_page, Login_page::class.java)
            startActivity(intent) // Mulai aktivitas login
            finish() // Tutup splash screen agar tidak kembali ke sini
        }, SPLASH_TIME_OUT)
    }

    // Fungsi untuk menambahkan animasi transisi saat splash screen selesai
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        // Animasi fade-in dan fade-out memberikan pengalaman transisi yang halus
    }
}
