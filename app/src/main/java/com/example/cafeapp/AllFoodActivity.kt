package com.example.cafeapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MenuDatabase.MenuAdapter
import com.example.cafeapp.MenuDatabase.MenuViewModel

class AllFoodActivity : AppCompatActivity() {
    private val menuViewModel: MenuViewModel by viewModels() // Get the MakanViewModel instance
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_food)

        // Inisialisasi RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.allFoodRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Layout grid dengan 2 kolom

        // Inisialisasi adapter dengan data kosong
        menuAdapter = MenuAdapter(emptyList()) { selectedMakan ->
            // Handle item click, misalnya buka detail makanan atau tambahkan ke keranjang
        }

        // Set adapter ke RecyclerView
        recyclerView.adapter = menuAdapter

        // Observe the allMakans LiveData from the MakanViewModel
        menuViewModel.getAllMakans().observe(this) { makanList ->
            // Update adapter dengan daftar makanan baru
            menuAdapter.updateData(makanList)
        }

    }
}
