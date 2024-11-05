package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.MakanDatabase.MakanAdapter
import com.example.cafeapp.MakanDatabase.MakanViewModel
import com.example.cafeapp.MinumDatabase.MinumListAdapter
import com.example.cafeapp.MinumDatabase.MinumViewModel
import com.example.cafeapp.databinding.ActivityAllFoodBinding

class AllFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllFoodBinding
    private val makanViewModel: MakanViewModel by viewModels()
    private val minumViewModel: MinumViewModel by viewModels()
    private lateinit var minumAdapter: MinumListAdapter
    private lateinit var makanAdapter: MakanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi RecyclerView untuk Makanan
        binding.allFoodRecyclerView.layoutManager = GridLayoutManager(this, 2)

        makanAdapter = MakanAdapter(emptyList()) { selectedMakan ->
            val intent = Intent(this, MenuDetailActivity::class.java).apply {
                putExtra("MAKAN_ID", selectedMakan._id.toString()) // Mengonversi ID ke String
            }
            startActivity(intent)
        }

        binding.allFoodRecyclerView.adapter = makanAdapter

        // Observe the allMakans LiveData
        makanViewModel.getAllMakans().observe(this) { makanList ->
            makanAdapter.updateData(makanList)
        }

        // Inisialisasi RecyclerView untuk Minuman
        binding.allMinumRecyclerView.layoutManager = GridLayoutManager(this, 2)

        minumAdapter = MinumListAdapter { selectedMinum ->
            val intent = Intent(this, MenuDetailActivity::class.java).apply {
                putExtra("MINUM_ID", selectedMinum._id.toString()) // Mengonversi ID ke String
            }
            startActivity(intent)
        }

        binding.allMinumRecyclerView.adapter = minumAdapter

        // Observe the allMinums LiveData
        minumViewModel.getAllMinums().observe(this) { minumList ->
            minumAdapter.submitList(minumList) // Menggunakan submitList untuk ListAdapter
        }
    }
}
