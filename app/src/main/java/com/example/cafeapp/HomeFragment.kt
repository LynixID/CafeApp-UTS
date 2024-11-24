package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.MenuDatabase.Kategori
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuAdapter
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        menuAdapter = MenuAdapter(emptyList()) { selectedMenu ->
            if (selectedMenu._id != 0) { // Pastikan bukan header
                val intent = Intent(requireContext(), MenuDetailActivity::class.java)
                intent.putExtra("MAKAN_ID", selectedMenu._id.toString())
                startActivity(intent)
            }
        }

        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val menu = menuAdapter.getMenuList().getOrNull(position)
                return if (menu?.nama == "Makanan" || menu?.nama == "Minuman") {
                    2
                } else {
                    1
                }
            }
        }

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = menuAdapter

        menuViewModel.filteredMakans.observe(viewLifecycleOwner) { makans ->
            val items = mutableListOf<Menu>()

            // Cek apakah ada item dengan kategori MAKAN
            if (makans.any { it.kategori == Kategori.MAKAN }) {
                // Tambahkan header untuk kategori MAKAN
                items.add(Menu(_id = 0, nama = "Makanan", harga = 0, deskripsi = "", kategori = Kategori.MAKAN, namaFoto = ""))
                // Tambahkan semua item dengan kategori MAKAN
                items.addAll(makans.filter { it.kategori == Kategori.MAKAN })
            }

            // Cek apakah ada item dengan kategori MINUM
            if (makans.any { it.kategori == Kategori.MINUM }) {
                // Tambahkan header untuk kategori MINUM
                items.add(Menu(_id = 0, nama = "Minuman", harga = 0, deskripsi = "", kategori = Kategori.MINUM, namaFoto = ""))
                // Tambahkan semua item dengan kategori MINUM
                items.addAll(makans.filter { it.kategori == Kategori.MINUM })
            }

            // Update adapter dengan data yang telah diolah
            menuAdapter.updateData(items)
        }

//        Setup ViewFlipper
        binding.viewFlipper.flipInterval = 3000
        binding.viewFlipper.startFlipping()


        setupUIComponents()
    }

    // Fungsi untuk menyiapkan komponen UI, seperti listener untuk pencarian dan filter
    private fun setupUIComponents() {
        // Menambahkan listener pada SearchView untuk pencarian menu
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Fungsi ini dipanggil saat teks pencarian dikirim
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { menuViewModel.searchItems(it) } // Memanggil fungsi pencarian pada ViewModel
                return true
            }

            // Fungsi ini dipanggil saat teks pencarian berubah
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { menuViewModel.searchItems(it) } // Memanggil fungsi pencarian saat teks berubah
                return true
            }
        })

        // Menambahkan listener untuk ikon filter (untuk memilih opsi sortir)
        binding.filterIcon.setOnClickListener { showSortOptions() }
    }

    // Fungsi untuk menampilkan dialog opsi sortir (A-Z, Z-A)
    private fun showSortOptions() {
        val sortOptions = arrayOf("A-Z", "Z-A") // Opsi sortir yang tersedia
        val sortDialog = AlertDialog.Builder(requireContext())
        sortDialog.setTitle("Sort") // Judul dialog
        sortDialog.setItems(sortOptions) { _, which ->
            // Menangani pemilihan opsi sortir
            when (which) {
                0 -> menuViewModel.sortItems(MenuViewModel.SortOrder.A_TO_Z) // Urutkan A-Z
                1 -> menuViewModel.sortItems(MenuViewModel.SortOrder.Z_TO_A) // Urutkan Z-A
            }
        }
        sortDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() } // Tombol batal
        sortDialog.create().show() // Menampilkan dialog sortir
    }

    // Fungsi ini dipanggil saat fragment dihancurkan untuk membersihkan referensi binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Melepas binding agar tidak terjadi kebocoran memori
    }
}
