package com.example.cafeapp.MenuDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cafeapp.Cart.CartManager
import com.example.cafeapp.Cart.CartViewModel
import com.example.cafeapp.Home.MainActivity
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.MenuDetailBinding
import java.io.File

// Aktivitas untuk menampilkan detail menu
class MenuDetailActivity : AppCompatActivity() {
    private lateinit var binding: MenuDetailBinding // Binding untuk layout
    private var quantity = 1 // Kuantitas default item yang ditambahkan ke keranjang
    private val cartViewModel: CartViewModel by viewModels() // ViewModel untuk keranjang belanja
    private val viewModel: MenuViewModel by viewModels() // ViewModel untuk menu

    // Fungsi utama saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuDetailBinding.inflate(layoutInflater) // Mengatur binding
        setContentView(binding.root)

        // Menambahkan listener untuk tombol back
        binding.buttonBackDetail.setOnClickListener {
            onBackPressed() // Memanggil fungsi default untuk kembali
        }

        // Mendapatkan ID menu dari Intent
        val makanId = intent.getStringExtra("MAKAN_ID")?.toIntOrNull()

        // Memastikan ID menu valid dan mengambil data menu dari ViewModel
        if (makanId != null && makanId != 0) {
            viewModel.getMakanById(makanId).observe(this) { makan ->
                if (makan != null) {
                    setupProductDetails(makan) // Menampilkan detail menu jika ditemukan
                } else {
                    Toast.makeText(this, "Menu tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish() // Menutup aktivitas jika menu tidak ditemukan
                }
            }
        } else {
            Toast.makeText(this, "ID menu tidak valid", Toast.LENGTH_SHORT).show()
            finish() // Menutup aktivitas jika ID tidak valid
        }
    }

    // Fungsi untuk mengatur detail menu
    private fun setupProductDetails(menu: Menu) {
        binding.namefood.text = menu.nama // Menampilkan nama menu
        binding.pricefood.text = "Rp ${menu.harga}" // Menampilkan harga menu
        binding.deskfood.text = menu.deskripsi // Menampilkan deskripsi menu

        // Memuat gambar menu dari penyimpanan internal atau menggunakan gambar default
        val imgPath = File(filesDir, "app_images/${menu.namaFoto}")
        if (imgPath.exists()) {
            loadImage(Uri.fromFile(imgPath)) // Memuat gambar dari file jika ada
        } else {
            loadImage(Uri.parse("android.resource://${packageName}/drawable/sample_image")) // Gambar default jika tidak ada
        }

        // Menambahkan listener untuk tombol "Add to Cart"
        binding.buttonAddToCart.setOnClickListener {
            addToCart(menu) // Menambahkan menu ke keranjang
        }
    }

    // Fungsi untuk memuat gambar dengan Glide
    private fun loadImage(imageUri: Uri) {
        Glide.with(this)
            .load(imageUri)
            .placeholder(R.drawable.sample_image) // Menampilkan gambar placeholder saat menunggu
            .into(binding.fotofood) // Menampilkan gambar ke ImageView
    }

    // Fungsi untuk menambahkan item ke keranjang
    private fun addToCart(menu: Menu) {
        val priceString = binding.pricefood.text.toString().replace("Rp ", "").replace(".", "").trim()
        val priceDouble = priceString.toDoubleOrNull() ?: 0.0 // Mengonversi harga menjadi double

        // Membuat objek CartItem untuk item yang ditambahkan ke keranjang
        val cartItem = CartItem(
            id = menu._id,
            name = binding.namefood.text.toString(),
            price = priceDouble.toString(),
            imageResId = menu.namaFoto,
            quantity = 1,
            category = menu.kategori
        )

        // Menambahkan item ke keranjang menggunakan CartManager
        CartManager.addItem(cartItem)
        Toast.makeText(this, "Item ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
        navigateToHome() // Navigasi ke halaman utama setelah item ditambahkan
    }

    // Fungsi untuk menavigasi ke halaman utama
    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("NAVIGATE_TO_CART", true) // Menyertakan informasi untuk navigasi ke keranjang
        startActivity(intent)
        finish() // Menutup aktivitas MenuDetailActivity
    }

    // Override fungsi onBackPressed untuk menangani tombol back sistem Android
    override fun onBackPressed() {
        super.onBackPressed() // Menutup aktivitas dan kembali ke aktivitas sebelumnya
    }
}