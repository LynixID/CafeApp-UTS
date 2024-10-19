package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
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
import com.example.cafeapp.MakanDatabase.MakanAdapter
import com.example.cafeapp.MakanDatabase.MakanViewModel
import com.example.cafeapp.MenuDetailActivity // Pastikan untuk mengimpor MenuDetailActivity

class HomeFragment : Fragment() {
    private lateinit var makanViewModel: MakanViewModel
    private lateinit var makanAdapter: MakanAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makanViewModel = ViewModelProvider(this).get(MakanViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recommendedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Inisialisasi adapter dengan daftar awal kosong
        makanAdapter = MakanAdapter(emptyList()) { selectedMakan ->
            // Handle item click event here
            val intent = Intent(requireContext(), MenuDetailActivity::class.java).apply {
                putExtra("EXTRA_NAMA", selectedMakan.name)
                putExtra("EXTRA_HARGA", selectedMakan.harga)
                putExtra("EXTRA_FOTO", selectedMakan.imagePath) // Pastikan ini adalah nama path yang benar
//                putExtra("EXTRA_DESKRIPSI", selectedMakan.deskripsi) // Jika Anda memiliki deskripsi di model Makan
            }
            startActivity(intent) // Navigasi ke MenuDetailActivity
        }
        recyclerView.adapter = makanAdapter

        // Mengamati data yang difilter
        makanViewModel.filteredMakans.observe(viewLifecycleOwner) { filteredList ->
            // Update UI dengan daftar makanan yang difilter
            makanAdapter.updateData(filteredList)
        }

        val seeAllTextView = view.findViewById<TextView>(R.id.seeAll)
        seeAllTextView.setOnClickListener {
            val intent = Intent(requireContext(), AllFoodActivity::class.java)
            startActivity(intent)
        }

        // Search functionality using SearchView
        val searchView = view.findViewById<SearchView>(R.id.searchView) // Pastikan SearchView ada di layout
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        val filterIcon = view.findViewById<ImageView>(R.id.filterIcon)
        filterIcon.setOnClickListener { showSortOptions() }
    }

    private fun showSortOptions() {
        val sortOptions = arrayOf("A-Z", "Z-A")

        val sortDialog = AlertDialog.Builder(requireContext())
        sortDialog.setTitle("Sort")
        sortDialog.setItems(sortOptions) { _, which ->
            when (which) {
                0 -> makanViewModel.sortItems(MakanViewModel.SortOrder.A_TO_Z) // Sort A-Z
                1 -> makanViewModel.sortItems(MakanViewModel.SortOrder.Z_TO_A) // Sort Z-A
            }
        }
        sortDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        sortDialog.create().show()
    }
}
