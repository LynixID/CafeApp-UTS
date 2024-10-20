package com.example.cafeapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MakanDatabase.MakanAdapter
import com.example.cafeapp.MakanDatabase.MakanViewModel

class AllFoodActivity : AppCompatActivity() {
    private val makanViewModel: MakanViewModel by viewModels() // Get the MakanViewModel instance
    private lateinit var makanAdapter: MakanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_food)

        // Inisialisasi RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.allFoodRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Layout grid dengan 2 kolom

        // Inisialisasi adapter dengan data kosong
        makanAdapter = MakanAdapter(emptyList()) { selectedMakan ->
            // Handle item click, misalnya buka detail makanan atau tambahkan ke keranjang
        }

        // Set adapter ke RecyclerView
        recyclerView.adapter = makanAdapter

        // Observe the allMakans LiveData from the MakanViewModel
        makanViewModel.allMakans.observe(this) { makanList ->
            // Update adapter dengan daftar makanan baru
            makanAdapter.updateData(makanList)
        }

        // Jika Anda ingin menerapkan filter dan sorting, panggil metode ini
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
        // Implementasikan logika filter dan sorting di sini jika diperlukan
    }
}
