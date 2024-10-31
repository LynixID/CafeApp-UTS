package com.example.cafeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.MakanDatabase.MakanViewModel
import com.example.cafeapp.databinding.MenuDetailBinding // Import the generated binding class
import java.io.File

class MenuDetailActivity : AppCompatActivity() {

    private lateinit var binding: MenuDetailBinding // Declare the binding variable

    private val cardViewModel: CardViewModel by viewModels()
    private val viewModel: MakanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuDetailBinding.inflate(layoutInflater) // Inflate the binding
        setContentView(binding.root) // Set the content view to the root of the binding

        // Ambil ID dari Intent
        val makanIdString = intent.getStringExtra("MAKAN_ID")
        val makanId = makanIdString?.toIntOrNull()

        makanId?.let { id ->
            viewModel.getMakanById(id).observe(this) { makan ->
                makan?.let {
                    setupProductDetails(it)
                }
            }
        }

        binding.buttonBackDetail.setOnClickListener {
            finish()
        }
    }

    private fun setupProductDetails(makan: Makan) {
        binding.namefood.text = makan.name
        binding.pricefood.text = "Rp ${makan.harga}"
        binding.deskfood.text = makan.deskripsi

        // Ambil path gambar dari direktori internal
        val imgPath = File(filesDir, "app_images/${makan.namaFoto}")

        // Pastikan gambar yang dimuat benar
        if (imgPath.exists()) {
            loadImage(Uri.fromFile(imgPath))
        } else {
            loadImage(Uri.parse("android.resource://${packageName}/drawable/sample_image")) // Gambar default jika tidak ada
        }

        binding.buttonAddToCart.setOnClickListener {
            addToCart(makan)
        }
    }

    private fun loadImage(imageUri: Uri) {
        // Menggunakan Glide untuk memuat gambar
        Glide.with(this)
            .load(imageUri)
            .placeholder(R.drawable.sample_image) // Placeholder jika gambar belum tersedia
            .into(binding.fotofood) // Use the binding for image loading
    }

    private fun addToCart(makan: Makan) {
        val priceString = binding.pricefood.text.toString().replace("Rp ", "").replace(".", "").trim()
        val priceDouble = priceString.toDoubleOrNull() ?: 0.0

        val cartItem = CartItem(
            id = makan._id, // Use the Makan ID as the cart item ID
            name = binding.namefood.text.toString(),
            price = priceDouble.toString(),
            imageResId = makan.namaFoto, // Menggunakan nama foto dari objek Makan
            quantity = 1  // Always start with quantity 1 when adding from menu
        )

        cardViewModel.addItem(cartItem)
        Toast.makeText(this, "Item ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()

        // Navigate to CartFragment
        navigateToCart()
    }

    private fun navigateToCart() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("NAVIGATE_TO_CART", true) // Optional: Kirim data untuk navigasi otomatis
        startActivity(intent)
        finish() // Menutup MenuDetailActivity setelah navigasi
    }
}
