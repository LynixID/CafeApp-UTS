package com.example.cafeapp.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.MenuDatabase.Kategori
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuAdapter
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.MenuDetail.MenuDetailActivity
import com.example.cafeapp.databinding.FragmentHomeBinding

// Fragment untuk halaman Home
class HomeFragment : Fragment() {
    // Variabel binding untuk mengakses elemen tampilan
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var menuAdapter: MenuAdapter

    // Fungsi untuk membuat tampilan fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Fungsi yang dipanggil setelah tampilan dibuat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel dan Adapter
        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        menuAdapter = MenuAdapter(emptyList()) { selectedMenu ->
            if (selectedMenu._id != 0) { // Cek apakah item bukan header
                val intent = Intent(requireContext(), MenuDetailActivity::class.java)
                intent.putExtra("MAKAN_ID", selectedMenu._id.toString())
                startActivity(intent) // Navigasi ke MenuDetailActivity
            }
        }

        // Mengatur GridLayoutManager untuk RecyclerView
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val menu = menuAdapter.getMenuList().getOrNull(position)
                return if (menu?.nama == "Makanan" || menu?.nama == "Minuman") {
                    2 // Header mengambil seluruh lebar
                } else {
                    1 // Item biasa mengambil setengah lebar
                }
            }
        }

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = menuAdapter

        // Observasi perubahan data menu
        menuViewModel.filteredMakans.observe(viewLifecycleOwner) { makans ->
            val items = mutableListOf<Menu>()

            // Tambahkan header dan item untuk kategori MAKAN
            if (makans.any { it.kategori == Kategori.MAKAN }) {
                items.add(Menu(_id = 0, nama = "Makanan", harga = 0, deskripsi = "", kategori = Kategori.MAKAN, namaFoto = ""))
                items.addAll(makans.filter { it.kategori == Kategori.MAKAN })
            }

            // Tambahkan header dan item untuk kategori MINUM
            if (makans.any { it.kategori == Kategori.MINUM }) {
                items.add(Menu(_id = 0, nama = "Minuman", harga = 0, deskripsi = "", kategori = Kategori.MINUM, namaFoto = ""))
                items.addAll(makans.filter { it.kategori == Kategori.MINUM })
            }

            menuAdapter.updateData(items) // Perbarui data di adapter
        }

        // Pengaturan ViewFlipper untuk slideshow
        binding.viewFlipper.flipInterval = 3000
        binding.viewFlipper.startFlipping()

        setupUIComponents() // Inisialisasi komponen UI
    }

    // Fungsi untuk menyiapkan komponen UI, seperti pencarian dan filter
    private fun setupUIComponents() {
        // Listener untuk SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { menuViewModel.searchItems(it) } // Pencarian item
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { menuViewModel.searchItems(it) } // Pencarian saat teks berubah
                return true
            }
        })

        // Listener untuk ikon filter
        binding.filterIcon.setOnClickListener { showSortOptions() }
    }

    // Fungsi untuk menampilkan dialog opsi sortir
    private fun showSortOptions() {
        val sortOptions = arrayOf("A-Z", "Z-A") // Opsi sortir
        val sortDialog = AlertDialog.Builder(requireContext())
        sortDialog.setTitle("Sort")
        sortDialog.setItems(sortOptions) { _, which ->
            when (which) {
                0 -> menuViewModel.sortItems(MenuViewModel.SortOrder.A_TO_Z) // Sortir A-Z
                1 -> menuViewModel.sortItems(MenuViewModel.SortOrder.Z_TO_A) // Sortir Z-A
            }
        }
        sortDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() } // Tombol batal
        sortDialog.create().show()
    }

    // Fungsi untuk membersihkan binding saat fragment dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Hindari kebocoran memori
    }
}