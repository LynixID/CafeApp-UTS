package com.example.cafeapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: AddToCardAdapter
    private val viewModel: CardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonBack: ImageButton = findViewById(R.id.btnBack)
        val buttonCheckout: Button = findViewById(R.id.btnCheckout)

        // Tombol back untuk kembali ke halaman sebelumnya
        buttonBack.setOnClickListener {
            finish()
        }

        // Tombol Checkout
        buttonCheckout.setOnClickListener {
            // Implementasikan logika untuk pembelian langsung
        }

        // Observe LiveData untuk cartItems dari ViewModel
        viewModel.cartItems.observe(this, Observer { items ->
            cartAdapter = AddToCardAdapter(this, items)
            recyclerView.adapter = cartAdapter
        })
    }
}

