package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MenuDatabase.MenuAdapter
import com.example.cafeapp.MenuDatabase.MenuViewModel

class HomeFragment : Fragment() {
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel here
        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        // Observe the filteredMakans LiveData after initialization
        menuViewModel.filteredMakans.observe(viewLifecycleOwner) { makans ->
            menuAdapter.updateData(makans)
        }


        val recyclerView = view.findViewById<RecyclerView>(R.id.recommendedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Initialize the adapter with an initial empty list
        menuAdapter = MenuAdapter(emptyList()) { selectedMakan ->
            // Handle item click event here
            val intent = Intent(requireContext(), MenuDetailActivity::class.java).apply {
                putExtra("MAKAN_ID", selectedMakan._id.toString()) // Mengirim ID makanan yang dipilih sebagai String
            }
            startActivity(intent) // Navigate to MenuDetailActivity
        }
        recyclerView.adapter = menuAdapter



        val seeAllTextView = view.findViewById<TextView>(R.id.seeAll)
        seeAllTextView.setOnClickListener {
            val intent = Intent(requireContext(), AllFoodActivity::class.java)
            startActivity(intent)
        }

        // Search functionality using SearchView
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { menuViewModel.searchItems(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { menuViewModel.searchItems(it) }
                return true
            }
        })

        // Filter and sort options
        val filterIcon = view.findViewById<ImageView>(R.id.filterIcon)
        filterIcon.setOnClickListener { showSortOptions() }
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
}
