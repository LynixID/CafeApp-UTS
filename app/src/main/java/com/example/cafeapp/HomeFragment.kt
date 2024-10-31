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
import com.example.cafeapp.MakanDatabase.MakanListAdapter
import com.example.cafeapp.MakanDatabase.MakanViewModel
import com.example.cafeapp.MinumDatabase.MinumViewModel
import com.example.cafeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var makanViewModel: MakanViewModel
    private lateinit var minumViewModel: MinumViewModel
    private lateinit var makanAdapter: MakanListAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModels
        makanViewModel = ViewModelProvider(this).get(MakanViewModel::class.java)
        minumViewModel = ViewModelProvider(this).get(MinumViewModel::class.java)

        // Observe the filteredMakans LiveData
        makanViewModel.filteredMakans.observe(viewLifecycleOwner) { makans ->
            makanAdapter.submitList(makans)
        }

        // Setup RecyclerView
        binding.recommendedRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Initialize the adapter
        makanAdapter = MakanListAdapter { selectedMakan ->
            val intent = Intent(requireContext(), MenuDetailActivity::class.java).apply {
                putExtra("MAKAN_ID", selectedMakan._id.toString())
            }
            startActivity(intent)
        }
        binding.recommendedRecyclerView.adapter = makanAdapter

        // Set up "See All" click listener
        binding.seeAll.setOnClickListener {
            val intent = Intent(requireContext(), AllFoodActivity::class.java)
            startActivity(intent)
        }

        // Search functionality using SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { makanViewModel.searchItems(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { makanViewModel.searchItems(it) }
                return true
            }
        })

        // Filter and sort options
        binding.filterIcon.setOnClickListener { showSortOptions() }

        // Category click listeners
        binding.categoryBurger.setOnClickListener {
            makanViewModel.filterByCategory("makanan") // Ganti dengan kategori yang sesuai
        }

        binding.categoryPasta.setOnClickListener {
            minumViewModel.filterByCategory("minuman") // Ganti dengan kategori yang sesuai
        }
    }

    private fun showSortOptions() {
        val sortOptions = arrayOf("A-Z", "Z-A")

        val sortDialog = AlertDialog.Builder(requireContext())
        sortDialog.setTitle("Sort")
        sortDialog.setItems(sortOptions) { _, which ->
            when (which) {
                0 -> makanViewModel.sortItems(MakanViewModel.SortOrder.A_TO_Z)
                1 -> makanViewModel.sortItems(MakanViewModel.SortOrder.Z_TO_A)
            }
        }
        sortDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        sortDialog.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding reference to prevent memory leaks
    }
}

//package com.example.cafeapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.SearchView
//import androidx.appcompat.app.AlertDialog
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.cafeapp.MakanDatabase.MakanListAdapter
//import com.example.cafeapp.MakanDatabase.MakanViewModel
//import com.example.cafeapp.databinding.FragmentHomeBinding
//
//class HomeFragment : Fragment() {
//    private lateinit var makanViewModel: MakanViewModel
//    private lateinit var makanAdapter: MakanListAdapter
//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        // Inflate the layout using View Binding
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize the ViewModel
//        makanViewModel = ViewModelProvider(this).get(MakanViewModel::class.java)
//
//        // Observe the filteredMakans LiveData after initialization
//        makanViewModel.filteredMakans.observe(viewLifecycleOwner) { makans ->
//            makanAdapter.submitList(makans)
//        }
//
//        // Setup RecyclerView
//        binding.recommendedRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//
//        // Initialize the adapter
//        makanAdapter = MakanListAdapter { selectedMakan ->
//            // Handle item click event here
//            val intent = Intent(requireContext(), MenuDetailActivity::class.java).apply {
//                putExtra("MAKAN_ID", selectedMakan._id.toString())
//            }
//            startActivity(intent)
//        }
//        binding.recommendedRecyclerView.adapter = makanAdapter
//
//        // Set up "See All" click listener
//        binding.seeAll.setOnClickListener {
//            val intent = Intent(requireContext(), AllFoodActivity::class.java)
//            startActivity(intent)
//        }
//
//        // Search functionality using SearchView
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let { makanViewModel.searchItems(it) }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                newText?.let { makanViewModel.searchItems(it) }
//                return true
//            }
//        })
//
//        // Filter and sort options
//        binding.filterIcon.setOnClickListener { showSortOptions() }
//    }
//
//    private fun showSortOptions() {
//        val sortOptions = arrayOf("A-Z", "Z-A")
//
//        val sortDialog = AlertDialog.Builder(requireContext())
//        sortDialog.setTitle("Sort")
//        sortDialog.setItems(sortOptions) { _, which ->
//            when (which) {
//                0 -> makanViewModel.sortItems(MakanViewModel.SortOrder.A_TO_Z)
//                1 -> makanViewModel.sortItems(MakanViewModel.SortOrder.Z_TO_A)
//            }
//        }
//        sortDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
//        sortDialog.create().show()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null // Clear binding reference to prevent memory leaks
//    }
//}