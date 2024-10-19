package com.example.cafeapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.cafeapp.R

class MenuDetailActivity : AppCompatActivity() {

    // Menggunakan ViewModel
    private val viewModel: MenuDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_detail)

        // Ambil data dari Intent dan pastikan untuk menangani nullability
        val menuName = intent.getStringExtra("MENU_NAME") ?: "Nama Tidak Ditemukan"
        val menuPrice = intent.getStringExtra("MENU_PRICE") ?: "Harga Tidak Ditemukan"
        val menuDescription = intent.getStringExtra("MENU_DESCRIPTION") ?: "Deskripsi Tidak Ditemukan"
        val menuImageResId = intent.getIntExtra("MENU_IMAGE_RES_ID", -1)

        // Logging untuk debugging
        Log.d("MenuDetailActivity", "Menu Image Res ID: $menuImageResId")

        // Referensi ke view
        val buttonBack: ImageButton = findViewById(R.id.buttonBackDetail)
        val imageMenu: ImageView = findViewById(R.id.imageProduct)
        val textMenuName: TextView = findViewById(R.id.textProductName)
        val textMenuPrice: TextView = findViewById(R.id.textProductPrice)
        val textMenuDescription: TextView = findViewById(R.id.textProductDescription)
        val buttonAddToCart: Button = findViewById(R.id.buttonAddToCart)
        val buttonBuyNow: Button = findViewById(R.id.buttonBuyNow)

        // Set data ke ViewModel
        viewModel.setMenuDetails(menuName, menuPrice, menuDescription, menuImageResId)

        // Observe LiveData dari ViewModel dan update UI saat ada perubahan data
        viewModel.menuName.observe(this, Observer { name ->
            textMenuName.text = name
        })

        viewModel.menuPrice.observe(this, Observer { price ->
            textMenuPrice.text = price
        })

        viewModel.menuDescription.observe(this, Observer { description ->
            textMenuDescription.text = description
        })

        viewModel.menuImageResId.observe(this, Observer { imageResId ->
            if (imageResId != -1) {
                imageMenu.setImageResource(imageResId)
            } else {
                imageMenu.setImageResource(R.drawable.ic_launcher_background) // Ganti dengan gambar default
            }
        })

        // Tombol back untuk kembali ke halaman sebelumnya
        buttonBack.setOnClickListener {
            finish()
        }

        // Tombol Add to Cart
        buttonAddToCart.setOnClickListener {
            // Implementasikan logika menambahkan ke keranjang
        }

        // Tombol Buy Now
        buttonBuyNow.setOnClickListener {
            // Implementasikan logika untuk pembelian langsung
        }
    }
}
