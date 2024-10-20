package com.example.cafeapp

import android.os.Bundle
import android.util.Log
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

class MenuDetailActivity : AppCompatActivity() {

    private lateinit var imageProduct: ImageView
    private lateinit var nameFood: TextView
    private lateinit var priceFood: TextView
    private lateinit var descriptionFood: TextView
    private lateinit var addToCartButton: Button
    private lateinit var backButton: ImageButton
    private var quantity = 1

    private val cardViewModel: CardViewModel by viewModels() // Inisialisasi CardViewModel
    private val viewModel: MakanViewModel by viewModels() // Inisialisasi MakanViewModel

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
        val makanId = intent.getStringExtra("MAKAN_ID") // Mengambil ID makanan sebagai String

        // Memeriksa apakah ID valid
        makanId?.let { id ->
            viewModel.getMakanById(id).observe(this) { makan ->
                makan?.let {
                    // Logging untuk memastikan data benar
                    Log.d("MenuDetailActivity", "Makan: $makan")
                    // Mengatur data produk
                    setupProductDetails(it)
                }
            }
        }

        // Mengatur klik pada tombol Back
        backButton.setOnClickListener {
            finish() // Kembali ke aktivitas sebelumnya
        }
    }

    // Fungsi untuk mengatur detail produk
    private fun setupProductDetails(makan: Makan) {
        nameFood.text = makan.name
        priceFood.text = "Rp ${makan.harga}"
        descriptionFood.text = makan.desk // Menampilkan deskripsi
        loadImage(makan.imagePath) // Fungsi untuk memuat gambar

        // Menambahkan logika untuk menambahkan ke keranjang
        addToCartButton.setOnClickListener {
            addToCart(makan)
        }
    }

    // Fungsi untuk memuat gambar dari path dengan Glide
    private fun loadImage(imagePath: String) {
        Glide.with(this)
            .load(imagePath)
            .placeholder(R.drawable.sample_image)
            .into(imageProduct)
    }

    // Fungsi untuk menambahkan item ke keranjang
    private fun addToCart(makan: Makan) {
        val priceString = priceFood.text.toString().replace("Rp ", "").replace(".", "").trim()
        val priceDouble = priceString.toDoubleOrNull() ?: 0.0

        val cartItem = CartItem(
            name = nameFood.text.toString(),
            price = priceDouble.toString(),
            imagePath = makan.imagePath,
            quantity = quantity
        )

        // Tambahkan item ke keranjang menggunakan CardViewModel
        cardViewModel.addItem(cartItem)

        Toast.makeText(this, "Item ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()

        // Pindah ke CartFragment setelah menambahkan item
        loadCardFragment()
    }

    // Fungsi untuk memuat CardFragment
    private fun loadCardFragment() {
        val cardFragment = CartFragment() // Buat instance dari CartFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, cardFragment) // Ganti dengan container yang sesuai
            .addToBackStack(null) // Tambahkan ke back stack jika ingin kembali
            .commit()
    }
}
