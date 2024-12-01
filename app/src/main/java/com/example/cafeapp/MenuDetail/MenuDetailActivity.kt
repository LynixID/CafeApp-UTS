package com.example.cafeapp.MenuDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cafeapp.Cart.CardViewModel
import com.example.cafeapp.Cart.CartManager
import com.example.cafeapp.Home.MainActivity
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.MenuDetailBinding
import java.io.File

class MenuDetailActivity : AppCompatActivity() {
    private lateinit var binding: MenuDetailBinding
    private var quantity = 1
    private val cardViewModel: CardViewModel by viewModels()
    private val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menambahkan listener untuk tombol back
        binding.buttonBackDetail.setOnClickListener {
            onBackPressed() // Akan memanggil fungsi default untuk kembali
        }

        val makanId = intent.getStringExtra("MAKAN_ID")?.toIntOrNull()

        if (makanId != null && makanId != 0) {
            viewModel.getMakanById(makanId).observe(this) { makan ->
                if (makan != null) {
                    setupProductDetails(makan)
                } else {
                    Toast.makeText(this, "Menu tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "ID menu tidak valid", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupProductDetails(menu: Menu) {
        binding.namefood.text = menu.nama
        binding.pricefood.text = "Rp ${menu.harga}"
        binding.deskfood.text = menu.deskripsi

        val imgPath = File(filesDir, "app_images/${menu.namaFoto}")
        if (imgPath.exists()) {
            loadImage(Uri.fromFile(imgPath))
        } else {
            loadImage(Uri.parse("android.resource://${packageName}/drawable/sample_image"))
        }

        binding.buttonAddToCart.setOnClickListener {
            addToCart(menu)
        }
    }

    private fun loadImage(imageUri: Uri) {
        Glide.with(this)
            .load(imageUri)
            .placeholder(R.drawable.sample_image)
            .into(binding.fotofood)
    }

    private fun addToCart(menu: Menu) {
        val priceString = binding.pricefood.text.toString().replace("Rp ", "").replace(".", "").trim()
        val priceDouble = priceString.toDoubleOrNull() ?: 0.0

        val cartItem = CartItem(
            id = menu._id,
            name = binding.namefood.text.toString(),
            price = priceDouble.toString(),
            imageResId = menu.namaFoto,
            quantity = 1,
            category = menu.kategori
        )

        CartManager.addItem(cartItem)
        Toast.makeText(this, "Item ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
        navigateToHome()
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("NAVIGATE_TO_CART", true)
        startActivity(intent)
        finish()
    }

    // Override fungsi onBackPressed untuk menangani tombol back sistem Android
    override fun onBackPressed() {
        super.onBackPressed() // Ini akan menutup activity dan kembali ke screen sebelumnya
    }
}