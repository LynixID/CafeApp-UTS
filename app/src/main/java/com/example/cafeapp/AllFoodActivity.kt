package com.example.cafeapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.MenuDatabase.MenuAdapter
import com.example.cafeapp.MenuDatabase.MenuViewModel
import android.content.Intent
import com.example.cafeapp.databinding.ActivityAllFoodBinding

class AllFoodActivity : AppCompatActivity() {
    private val menuViewModel: MenuViewModel by viewModels() // Mendapatkan instance dari MenuViewModel
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var binding: ActivityAllFoodBinding // Declare the binding object

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding
        binding = ActivityAllFoodBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set the root view of the layout

        // Inisialisasi adapter dengan data kosong
        menuAdapter = MenuAdapter(emptyList()) { selectedMakan ->
            // Handle klik item untuk membuka MenuDetailActivity
            val intent = Intent(this, MenuDetailActivity::class.java).apply {
                putExtra("MAKAN_ID", selectedMakan._id.toString()) // Mengirimkan ID makanan sebagai String
            }
            startActivity(intent)
        }

        // Set RecyclerView with GridLayoutManager
        binding.allFoodRecyclerView.layoutManager = GridLayoutManager(this, 2) // Layout grid dengan 2 kolom
        binding.allFoodRecyclerView.adapter = menuAdapter

        // Observe LiveData untuk mendapatkan semua makanan dari ViewModel
        menuViewModel.getAllMakans().observe(this) { makanList ->
            // Update adapter dengan daftar makanan baru
            menuAdapter.updateData(makanList)
        }
    }
}
