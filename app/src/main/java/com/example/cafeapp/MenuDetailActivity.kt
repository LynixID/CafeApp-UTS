package com.example.cafeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.MakanDatabase.MakanViewModel
import com.example.cafeapp.MinumDatabase.Minum
import com.example.cafeapp.MinumDatabase.MinumViewModel
import com.example.cafeapp.databinding.MenuDetailBinding
import java.io.File

class MenuDetailActivity : AppCompatActivity() {
        private lateinit var binding: MenuDetailBinding
        private lateinit var cardViewModel: CardViewModel  // Change to lateinit
        private val viewModel: MakanViewModel by viewModels()
        private val minumViewModel: MinumViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = MenuDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Initialize cardViewModel dengan activity scope
            cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]

        // Ambil ID dari Intent
        val makanIdString = intent.getStringExtra("MAKAN_ID")
        val minumIdString = intent.getStringExtra("MINUM_ID")

        if (makanIdString == null && minumIdString == null) {
            Toast.makeText(this, "ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish() // Keluar dari Activity jika tidak ada ID
        }

        // Mengonversi ID ke Int jika makanIdString tidak null
        makanIdString?.toIntOrNull()?.let { makanId ->
            viewModel.getMakanById(makanId).observe(this) { makan ->
                makan?.let { setupProductDetails(it) }
            }
        }

        // Mengonversi ID ke Int jika minumIdString tidak null
        minumIdString?.toIntOrNull()?.let { minumId ->
            minumViewModel.getMinumById(minumId).observe(this) { minum ->
                minum?.let { setupProductDetails(it) }
            }
        }

        binding.buttonBackDetail.setOnClickListener {
            finish()
        }
    }

    private fun setupProductDetails(item: Any) {
        binding.buttonAddToCart.setOnClickListener { addToCart(item) }
        when (item) {
            is Makan -> {
                binding.namefood.text = item.name
                binding.pricefood.text = "Rp ${item.harga}"
                binding.deskfood.text = item.deskripsi
                loadImageFromFile(item.namaFoto)
            }
            is Minum -> {
                binding.namefood.text = item.name
                binding.pricefood.text = "Rp ${item.harga}"
                binding.deskfood.text = item.deskripsi
                loadImageFromFile(item.namaFoto)
            }
        }
    }

    private fun loadImageFromFile(namaFoto: String) {
        val imgPath = File(filesDir, "app_images/$namaFoto")
        val imageUri = if (imgPath.exists()) {
            Uri.fromFile(imgPath)
        } else {
            Uri.parse("android.resource://${packageName}/drawable/sample_image") // Gambar default
        }
        loadImage(imageUri)
    }

    private fun loadImage(imageUri: Uri) {
        Glide.with(this)
            .load(imageUri)
            .placeholder(R.drawable.sample_image) // Placeholder jika gambar belum tersedia
            .into(binding.fotofood)
    }

    private fun addToCart(item: Any) {
        val priceString = binding.pricefood.text.toString().replace("Rp ", "").replace(".", "").trim()
        val priceDouble = priceString.toDoubleOrNull() ?: 0.0

        val cartItem = when (item) {
            is Makan -> CartItem(
                id = item._id,
                name = item.name,
                price = priceDouble.toString(),
                imageResId = item.namaFoto,
                quantity = 1,
                category = "Makan"
            )
            is Minum -> CartItem(
                id = item._id,
                name = item.name,
                price = priceDouble.toString(),
                imageResId = item.namaFoto,
                quantity = 1,
                category = "Minum"
            )
            else -> return
        }

        // Tambahkan item ke CartManager
        CartManager.addItem(cartItem)
        // Refresh ViewModel
        cardViewModel.refreshItems()

        Toast.makeText(this, "Item ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
        navigateToCart()
    }

    private fun navigateToCart() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("NAVIGATE_TO_CART", true)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        finish()
    }
}
