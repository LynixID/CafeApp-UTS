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

    private lateinit var binding: MenuDetailBinding
    private var quantity = 1

    private val cardViewModel: CardViewModel by viewModels()
    private val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val makanIdString = intent.getStringExtra("MAKAN_ID")
        val makanId = makanIdString?.toIntOrNull()

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


        binding.buttonBackDetail.setOnClickListener {
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
}
