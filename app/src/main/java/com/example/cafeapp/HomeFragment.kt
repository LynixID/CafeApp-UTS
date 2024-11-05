package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.example.cafeapp.MakanDatabase.MakanAdapter
import com.example.cafeapp.MakanDatabase.MakanViewModel
import com.example.cafeapp.MenuDetailActivity
import com.example.cafeapp.MinumDatabase.MinumAdapter
import com.example.cafeapp.MinumDatabase.MinumListAdapter
import com.example.cafeapp.MinumDatabase.MinumViewModel
import com.example.cafeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var makanViewModel: MakanViewModel
    private lateinit var minumViewModel: MinumViewModel
    private lateinit var makanAdapter: MakanAdapter
    private lateinit var minumAdapter: MinumAdapter

    // Track current category filter state
    private var currentCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModels
        makanViewModel = ViewModelProvider(this).get(MakanViewModel::class.java)
        minumViewModel = ViewModelProvider(this).get(MinumViewModel::class.java)

        // Set up RecyclerViews
        setupRecyclerViews()

        // Set up category click listeners
        setupCategoryClickListeners()

        // Set up other UI components
        setupUIComponents()

        // Observe data changes
        observeViewModels()
    }

    private fun setupRecyclerViews() {
        binding.recommendedRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recommendedMinumRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        makanAdapter = MakanAdapter(emptyList()) { selectedMakan ->
            val intent = Intent(requireContext(), MenuDetailActivity::class.java).apply {
                putExtra("MAKAN_ID", selectedMakan._id.toString())
            }
            startActivity(intent)
        }
        binding.recommendedRecyclerView.adapter = makanAdapter

        minumAdapter = MinumAdapter(emptyList()) { selectedMinum ->
            val intent = Intent(requireContext(), MenuDetailActivity::class.java).apply {
                putExtra("MINUM_ID", selectedMinum._id.toString())
            }
            startActivity(intent)
        }
        binding.recommendedMinumRecyclerView.adapter = minumAdapter
    }

    private fun setupCategoryClickListeners() {
        Log.d("CLCIKED", "CLICKED")
        binding.categoryBurger.setOnClickListener {
            toggleCategoryFilter("Makanan", makanViewModel::filterByCategory)


        }

        binding.categoryPasta.setOnClickListener {
            toggleCategoryFilter("Minuman", minumViewModel::filterByCategory)



        }
    }

    private fun toggleCategoryFilter(category: String, filterFunction: (String) -> Unit) {
        Log.d("CATEGORY", category)
        if (currentCategory == category) {
            // Clear filter if the same category is clicked
            currentCategory = null
            makanViewModel.loadAllItems()
            minumViewModel.loadAllItems()
            binding.recommendedMinumRecyclerView.visibility = View.VISIBLE
            binding.recommendedRecyclerView.visibility = View.VISIBLE
            Log.d("IF", category)

        } else {
            Log.d("else", category)

            // Set new filter and clear previous
            currentCategory = category
            if (category == "Makanan") {
                makanViewModel.filterByCategory(category)
                minumViewModel.loadAllItems() // Reset minum list
                binding.recommendedMinumRecyclerView.visibility = View.GONE
                binding.recommendedRecyclerView.visibility = View.VISIBLE
            } else {
                minumViewModel.filterByCategory(category)
                makanViewModel.loadAllItems() // Reset makan list
                binding.recommendedMinumRecyclerView.visibility = View.VISIBLE
                binding.recommendedRecyclerView.visibility = View.GONE
            }
        }
        updateCategoryIconStates()
    }

    private fun updateCategoryIconStates() {
        // Update visual state of category icons based on current filter
        binding.categoryBurger.alpha = if (currentCategory == "Makanan") 1.0f else 0.5f
        binding.categoryPasta.alpha = if (currentCategory == "Minuman") 1.0f else 0.5f
    }

    private fun setupUIComponents() {
        // See All click listener
        binding.seeAll.setOnClickListener {
            val intent = Intent(requireContext(), AllFoodActivity::class.java)
            startActivity(intent)
        }

        // Search functionality
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchItems(newText)
                return true
            }
        })

        // Filter icon click listener
        binding.filterIcon.setOnClickListener { showSortOptions() }
    }

    private fun observeViewModels() {
        makanViewModel.filteredMakans.observe(viewLifecycleOwner) { makans ->
            if (makans.isEmpty()) {
                binding.recommendedRecyclerView.visibility = View.GONE
            } else {
                Log.d("MAKANS", makans.toString())
                binding.recommendedRecyclerView.visibility = View.VISIBLE
                makanAdapter.updateData(makans)
            }
            checkVisibility()
        }

        minumViewModel.filteredMinums.observe(viewLifecycleOwner) { minums ->
            if (minums.isEmpty()) {
                binding.recommendedMinumRecyclerView.visibility = View.GONE
            } else {
                Log.d("minums", minums.toString())
                binding.recommendedMinumRecyclerView.visibility = View.VISIBLE
                minumAdapter.updateData(minums)
            }
            checkVisibility()
        }
    }

    private fun checkVisibility() {
        // Hide the recommended sections if both lists are empty
        Log.d("VISIBILITY", "VISIBILITY")
        val bothEmpty = makanViewModel.filteredMakans.value.isNullOrEmpty() && minumViewModel.filteredMinums.value.isNullOrEmpty()
        if(bothEmpty) {
            binding.recommendedRecyclerView.visibility = View.GONE
            binding.recommendedMinumRecyclerView.visibility = View.GONE

        }else if(makanViewModel.filteredMakans.value.isNullOrEmpty()){
            binding.recommendedMinumRecyclerView.visibility = View.VISIBLE
            binding.recommendedRecyclerView.visibility = View.GONE
        }else if(minumViewModel.filteredMinums.value.isNullOrEmpty()){
            binding.recommendedMinumRecyclerView.visibility = View.GONE
            binding.recommendedRecyclerView.visibility = View.VISIBLE
        }else{
            binding.recommendedMinumRecyclerView.visibility = View.VISIBLE
            binding.recommendedRecyclerView.visibility = View.VISIBLE
        }
        //
        //        binding.recommendedRecyclerView.visibility = if (bothEmpty) View.GONE else View.VISIBLE
//        binding.recommendedMinumRecyclerView.visibility = if (bothEmpty) View.GONE else View.VISIBLE
    }

    private fun searchItems(query: String?) {
        if (query.isNullOrEmpty()) {
            Log.d("TIDAK MASUK","TIDAK MASUK")

            // If query is empty, show all recommended items
            makanViewModel.loadAllItems() // Reset the filtered list
            minumViewModel.loadAllItems() // Reset the filtered list
        } else {
            // Search in both models
            print("Search" + query)
            Log.d("MASUKK","MASUK")
            makanViewModel.searchItems(query)
            minumViewModel.searchItems(query)
        }
    }

    private fun showSortOptions() {
        val sortOptions = arrayOf("A-Z", "Z-A")

        val sortDialog = AlertDialog.Builder(requireContext())
        sortDialog.setTitle("Sort")
        sortDialog.setItems(sortOptions) { _, which ->
            when (which) {
                0 -> {
                    makanViewModel.sortItems(MakanViewModel.SortOrder.A_TO_Z)
                    minumViewModel.sortItems(MinumViewModel.SortOrder.A_TO_Z) // Sort A-Z for Minum
                }
                1 -> {
                    makanViewModel.sortItems(MakanViewModel.SortOrder.Z_TO_A)
                    minumViewModel.sortItems(MinumViewModel.SortOrder.Z_TO_A) // Sort Z-A for Minum
                }
            }
        }
        sortDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        sortDialog.create().show()
    }
}
