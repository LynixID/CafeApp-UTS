package com.example.cafeapp.Profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.databinding.ActivityTentangAplikasiBinding

// Aktivitas untuk menampilkan informasi tentang aplikasi
class TentangAplikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTentangAplikasiBinding // Binding untuk layout

    // Fungsi utama saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangAplikasiBinding.inflate(layoutInflater) // Mengatur binding
        setContentView(binding.root) // Mengatur konten tampilan

        // Tombol Back untuk kembali ke aktivitas sebelumnya
        binding.buttonBack.setOnClickListener {
            finish() // Menutup aktivitas ini dan kembali
        }
    }
}
