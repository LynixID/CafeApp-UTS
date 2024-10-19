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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_food)

        val recyclerView = findViewById<RecyclerView>(R.id.allFoodRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Vertical layout

        // Observe the allMakans LiveData from the MakanViewModel
        makanViewModel.getAllMakans().observe(this) { makanList ->
            recyclerView.adapter = MakanAdapter(makanList) { selectedMakan ->
                // Handle item click, e.g., show details or add to cart
            } // Update the adapter with new data
        }

        // Optionally, you can apply filters and sorting here if needed
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
        // This can be implemented if you want to filter or sort the data
    }
}
