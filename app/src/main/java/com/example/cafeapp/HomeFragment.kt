package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.MenuDatabase.MenuAdapter
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var menuAdapter: MenuAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        // Observe filteredMakans LiveData to update UI
        menuViewModel.filteredMakans.observe(viewLifecycleOwner) { makans ->
            menuAdapter.updateData(makans)
        }

        binding.recommendedRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        // Initialize the adapter with click listener for each item
        menuAdapter = MenuAdapter(emptyList()) { selectedMakan ->
            // Handle item click event to navigate to MenuDetailActivity
            val intent = Intent(requireContext(), MenuDetailActivity::class.java).apply {
                putExtra("MAKAN_ID", selectedMakan._id.toString()) // Pass the selected item's ID
            }
            startActivity(intent) // Navigate to MenuDetailActivity
        }
        binding.recommendedRecyclerView.adapter = menuAdapter

        setupCategoryClickListeners()
        setupUIComponents()

        binding.seeAll.setOnClickListener {
            // Tindakan saat tombol See All diklik
            val intent = Intent(requireContext(), AllFoodActivity::class.java)
            startActivity(intent) // Mulai AllFoodActivity
        }
    }

    // Setup listeners for category filtering
    private fun setupCategoryClickListeners() {
        binding.categoryBurger.setOnClickListener {
            toggleCategoryFilter("Makanan")
        }

        binding.categoryPasta.setOnClickListener {
            toggleCategoryFilter("Minuman")
        }
    }

    private var currentCategory: String? = null

    private fun toggleCategoryFilter(category: String) {
        if (currentCategory == category) {
            currentCategory = null
            menuViewModel.loadAllItems()
        } else {
            currentCategory = category
            menuViewModel.filterByCategory(category)
        }
        updateCategoryIconStates()
    }

    private fun updateCategoryIconStates() {
        binding.categoryBurger.alpha = if (currentCategory == "Makanan") 1.0f else 0.5f
        binding.categoryPasta.alpha = if (currentCategory == "Minuman") 1.0f else 0.5f
    }

    private fun setupUIComponents() {
        // Search functionality
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { menuViewModel.searchItems(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { menuViewModel.searchItems(it) }
                return true
            }
        })

        // Filter and sorting icon listener
        binding.filterIcon.setOnClickListener { showSortOptions() }
    }

    private fun showSortOptions() {
        val sortOptions = arrayOf("A-Z", "Z-A")

        val sortDialog = AlertDialog.Builder(requireContext())
        sortDialog.setTitle("Sort")
        sortDialog.setItems(sortOptions) { _, which ->
            when (which) {
                0 -> menuViewModel.sortItems(MenuViewModel.SortOrder.A_TO_Z) // Sort A-Z
                1 -> menuViewModel.sortItems(MenuViewModel.SortOrder.Z_TO_A) // Sort Z-A
            }
        }
        sortDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        sortDialog.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
