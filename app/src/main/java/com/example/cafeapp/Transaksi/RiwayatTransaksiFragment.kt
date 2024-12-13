package com.example.cafeapp.Transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.databinding.FragmentRiwayatTransaksiBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

// Kelas Fragment untuk menampilkan riwayat transaksi pengguna
class RiwayatTransaksiFragment : Fragment() {
    // Deklarasi variabel untuk database dan RecyclerView
    private lateinit var db: TransaksiDatabase
    private lateinit var binding: FragmentRiwayatTransaksiBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransaksiAdapter
    private var isNewestFirst = true  // Default untuk menampilkan transaksi terbaru terlebih dahulu

    // Menangani pembuatan tampilan untuk fragment ini
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menginflate layout menggunakan ViewBinding
        binding = FragmentRiwayatTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Menangani logika setelah tampilan fragment dibuat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mendapatkan instance dari TransaksiDatabase
        db = TransaksiDatabase.getInstance(requireContext())
        recyclerView = binding.recyclerViewRiwayatTransaksi

        // Mengatur RecyclerView
        setupRecyclerView()

        // Mengatur tombol kembali untuk navigasi ke halaman transaksi
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_RiwayatTransaksiFragment_to_TransaksiFragment)
        }

        // Mengatur Spinner untuk memilih urutan tampilan transaksi
        setupSortSpinner(view)

        // Memuat data transaksi awal
        loadTransactions()
    }

    // Mengatur layout RecyclerView menggunakan LinearLayoutManager
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    // Mengatur Spinner untuk memilih urutan transaksi (terbaru atau terlama)
    private fun setupSortSpinner(view: View) {
        val spinner = binding.sortSpinner
        // Mengisi Spinner dengan opsi urutan
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Menangani pemilihan item di Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Menentukan apakah urutan transaksi terbaru atau terlama yang dipilih
                isNewestFirst = position == 0  // Posisi 0 untuk "Terbaru"
                loadTransactions()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak ada aksi jika tidak ada yang dipilih
            }
        }
    }

    // Memuat dan menampilkan transaksi berdasarkan urutan yang dipilih
    private fun loadTransactions() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Mendapatkan daftar transaksi dari database
            val transaksiList = db.transaksiDao().getAllTransaksi()

            // Mengurutkan transaksi berdasarkan waktu (terbaru atau terlama)
            val sortedList = if (isNewestFirst) {
                // Jika yang terbaru di atas, urutkan secara menurun
                transaksiList.sortedByDescending {
                    try {
                        SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                        ).parse(it.waktu)?.time ?: 0
                    } catch (e: Exception) {
                        0
                    }
                }
            } else {
                // Jika yang terlama di atas, urutkan secara naik
                transaksiList.sortedBy {
                    try {
                        SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                        ).parse(it.waktu)?.time ?: 0
                    } catch (e: Exception) {
                        0
                    }
                }
            }

            // Menampilkan transaksi yang sudah diurutkan ke dalam RecyclerView
            withContext(Dispatchers.Main) {
                if (sortedList.isEmpty()) {
                    // Menampilkan pesan jika tidak ada transaksi
                    Toast.makeText(requireContext(), "Belum ada transaksi", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Menampilkan transaksi dalam RecyclerView menggunakan adapter
                    adapter = TransaksiAdapter(sortedList)
                    recyclerView.adapter = adapter
                }
            }
        }
    }
}