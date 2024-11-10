package com.example.cafeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.databinding.MenuDetailBinding
import java.io.File

class MenuDetailActivity : AppCompatActivity() {

    private lateinit var binding: MenuDetailBinding // Declare binding object
    private var quantity = 1

    private val cardViewModel: CardViewModel by viewModels()
    private val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuDetailBinding.inflate(layoutInflater) // Initialize the binding
        setContentView(binding.root) // Set the root view using the binding

        // Ambil ID dari Intent
        val makanIdString = intent.getStringExtra("MAKAN_ID")
        val makanId = makanIdString?.toIntOrNull() // Mengonversi string kembali ke Int

        // Mengonversi ID ke Int jika makanIdString tidak null
        makanIdString?.let { id ->
            val makanId = id.toIntOrNull()
            makanId?.let { id ->
                viewModel.getMakanById(id).observe(this) { makan ->
                    makan?.let {
                        setupProductDetails(it)
                    }
                }
            }
        }

        // Set back button listener
        binding.buttonBackDetail.setOnClickListener {
            finish()
        }
    }

    private fun setupProductDetails(menu: Menu) {
        // Use ViewBinding to set the values
        binding.namefood.text = menu.nama
        binding.pricefood.text = "Rp ${menu.harga}"
        binding.deskfood.text = menu.deskripsi

        // Ambil path gambar dari direktori internal
        val imgPath = File(filesDir, "app_images/${menu.namaFoto}")

        // Pastikan gambar yang dimuat benar
        if (imgPath.exists()) {
            loadImage(Uri.fromFile(imgPath))
        } else {
            loadImage(Uri.parse("android.resource://${packageName}/drawable/sample_image")) // Gambar default jika tidak ada
        }

        binding.buttonAddToCart.setOnClickListener {
            addToCart(menu)
        }
    }

    private fun loadImage(imageUri: Uri) {
        // Menggunakan Glide untuk memuat gambar
        Glide.with(this)
            .load(imageUri)
            .placeholder(R.drawable.sample_image) // Placeholder jika gambar belum tersedia
            .into(binding.fotofood)
    }

    private fun addToCart(menu: Menu) {
        val priceString = binding.pricefood.text.toString().replace("Rp ", "").replace(".", "").trim()
        val priceDouble = priceString.toDoubleOrNull() ?: 0.0

        val cartItem = CartItem(
            id = menu._id,                  // Use the Makan ID as the cart item ID
            name = binding.namefood.text.toString(),
            price = priceDouble.toString(),
            imageResId = menu.namaFoto,     // Use the photo name from the Menu object
            quantity = 1,                   // Start with quantity 1 when adding from the menu
            category = menu.kategori         // Pass the category from the Menu object
        )

        // Add item to cart using CartManager
        CartManager.addItem(cartItem)
        Toast.makeText(this, "Item ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()

        // Navigate to CartFragment
        navigateToHome()
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("NAVIGATE_TO_CART", true) // Optional: Kirim data untuk navigasi otomatis
        startActivity(intent)
        finish() // Menutup MenuDetailActivity setelah navigasi
    }
}
