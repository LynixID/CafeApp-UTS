package com.example.cafeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.MakanDatabase.MakanViewModel
import java.io.File

class MenuDetailActivity : AppCompatActivity() {

    private lateinit var imageProduct: ImageView
    private lateinit var nameFood: TextView
    private lateinit var priceFood: TextView
    private lateinit var descriptionFood: TextView
    private lateinit var addToCartButton: Button
    private lateinit var backButton: ImageButton
    private var quantity = 1

    private val cardViewModel: CardViewModel by viewModels()
    private val viewModel: MakanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_detail)

        // Inisialisasi UI
        imageProduct = findViewById(R.id.fotofood)
        nameFood = findViewById(R.id.namefood)
        priceFood = findViewById(R.id.pricefood)
        descriptionFood = findViewById(R.id.deskfood)
        addToCartButton = findViewById(R.id.buttonAddToCart)
        backButton = findViewById(R.id.buttonBackDetail)

        // Ambil ID dari Intent
        val makanIdString = intent.getStringExtra("MAKAN_ID")

        // Mengonversi ID ke Int jika makanIdString tidak null
        val makanId = makanIdString?.toIntOrNull()

        makanId?.let { id ->
            viewModel.getMakanById(id).observe(this) { makan ->
                makan?.let {
                    setupProductDetails(it)
                }
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupProductDetails(makan: Makan) {
        nameFood.text = makan.name
        priceFood.text = "Rp ${makan.harga}"
        descriptionFood.text = makan.deskripsi

        // Ambil path gambar dari direktori internal
        val imgPath = File(filesDir, "app_images/${makan.namaFoto}")

        // Pastikan gambar yang dimuat benar
        if (imgPath.exists()) {
            loadImage(Uri.fromFile(imgPath))
        } else {
            loadImage(Uri.parse("android.resource://${packageName}/drawable/sample_image")) // Gambar default jika tidak ada
        }

        addToCartButton.setOnClickListener {
            addToCart(makan)
        }
    }

    private fun loadImage(imageUri: Uri) {
        // Menggunakan Glide untuk memuat gambar
        Glide.with(this)
            .load(imageUri)
            .placeholder(R.drawable.sample_image) // Placeholder jika gambar belum tersedia
            .into(imageProduct)
    }

    private fun addToCart(makan: Makan) {
        val priceString = priceFood.text.toString().replace("Rp ", "").replace(".", "").trim()
        val priceDouble = priceString.toDoubleOrNull() ?: 0.0

        val cartItem = CartItem(
            id = makan._id, // Use the Makan ID as the cart item ID
            name = nameFood.text.toString(),
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
