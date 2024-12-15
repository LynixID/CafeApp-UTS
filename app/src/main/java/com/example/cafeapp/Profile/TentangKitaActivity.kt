package com.example.cafeapp.Profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.databinding.ActivityTentangKitaBinding

// Aktivitas untuk menampilkan informasi tentang pengembang atau tim (Tentang Kita)
class TentangKitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTentangKitaBinding // Binding untuk layout

    // Fungsi utama saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangKitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol Back untuk kembali ke aktivitas sebelumnya
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}
