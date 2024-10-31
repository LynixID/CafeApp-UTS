package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.MakanDatabase.MakanListAdapter
import com.example.cafeapp.MakanDatabase.MakanViewModel
import com.example.cafeapp.databinding.ActivityAllFoodBinding

class AllFoodActivity : AppCompatActivity() {
    private val makanViewModel: MakanViewModel by viewModels() // Mengambil instance MakanViewModel
    private lateinit var makanAdapter: MakanListAdapter
    private lateinit var binding: ActivityAllFoodBinding // Deklarasi binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding
        binding = ActivityAllFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi RecyclerView
        binding.allFoodRecyclerView.layoutManager = GridLayoutManager(this, 2) // Layout grid dengan 2 kolom

        // Inisialisasi adapter dengan data kosong
        makanAdapter = MakanListAdapter { selectedMakan ->
            // Handle item click
            val intent = Intent(this, MenuDetailActivity::class.java).apply {
                putExtra("MAKAN_ID", selectedMakan._id.toString()) // Mengirim ID makanan yang dipilih sebagai String
            }
            startActivity(intent) // Buka MenuDetailActivity untuk detail makanan
        }

        // Set adapter ke RecyclerView
        binding.allFoodRecyclerView.adapter = makanAdapter

        // Observe the allMakans LiveData dari MakanViewModel
        makanViewModel.getAllMakans().observe(this) { makanList ->
            // Update adapter dengan daftar makanan baru
            makanAdapter.submitList(makanList) // Menggunakan submitList untuk ListAdapter
        }
    }
}