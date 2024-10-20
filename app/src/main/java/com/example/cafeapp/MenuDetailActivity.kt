package com.example.cafeapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.cafeapp.MakanDatabase.MakanViewModel

class MenuDetailActivity : AppCompatActivity() {

    private lateinit var imageProduct: ImageView
    private lateinit var nameFood: TextView
    private lateinit var priceFood: TextView
    private lateinit var addToCartButton: Button
    private lateinit var backButton: ImageButton
    private var quantity = 1

    private val viewModel: MakanViewModel by viewModels() // Inisialisasi ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_detail)

        imageProduct = findViewById(R.id.fotofood)
        nameFood = findViewById(R.id.namefood)
        priceFood = findViewById(R.id.pricefood)
        addToCartButton = findViewById(R.id.buttonAddToCart)
        backButton = findViewById(R.id.buttonBackDetail)

        // Ambil ID dari Intent
        val makanId = intent.getIntExtra("MAKAN_ID", -1)

        // Memeriksa apakah ID valid
        if (makanId != -1) {
            viewModel.getMakanById(makanId).observe(this, Observer { makan ->
                if (makan != null) {
                    // Mengatur data produk
                    nameFood.text = makan.name
                    priceFood.text = "Rp ${makan.harga}"
                    loadImage(makan.imagePath) // Fungsi untuk memuat gambar
                }
            })
        }

        // Mengatur klik pada tombol Add to Cart
        addToCartButton.setOnClickListener {
            val priceString = priceFood.text.toString().replace("Rp ", "").replace(".", "").trim()
            val priceDouble = if (priceString.isNotEmpty()) {
                priceString.toDouble()
            } else {
                0.0 // Nilai default jika harga kosong
            }

            val cartItem = CartItem(
                name = nameFood.text.toString(),
                price = priceDouble.toString(), // Pastikan ini adalah string harga yang valid
                imageResId = R.drawable.sample_image, // Ganti dengan ID gambar yang sesuai
                quantity = quantity
            )
            CartManager.addItem(cartItem) // Tambahkan item ke keranjang
            Toast.makeText(this, "Item ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
        }

        // Mengatur klik pada tombol Back
        backButton.setOnClickListener {
            finish() // Kembali ke aktivitas sebelumnya
        }
    }

    private fun loadImage(imagePath: String) {
        // Di sini Anda dapat menggunakan Glide atau Picasso untuk memuat gambar
        // Contoh menggunakan Glide:
        // Glide.with(this).load(File(imagePath)).into(imageProduct)
        imageProduct.setImageResource(R.drawable.sample_image) // Ganti dengan logika pemuatan gambar yang sesuai
    }
}
