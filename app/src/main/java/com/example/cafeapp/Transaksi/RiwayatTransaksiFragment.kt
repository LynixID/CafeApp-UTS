package com.example.cafeapp.Transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RiwayatTransaksiFragment : Fragment() {

    private lateinit var transaksiDao: TransaksiDAO
    private lateinit var recyclerView: RecyclerView
    private lateinit var transaksiAdapter: TransaksiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_riwayat_transaksi, container, false)

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewRiwayatTransaksi)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Inisialisasi TransaksiDAO
        transaksiDao = TransaksiDatabase.getInstance(requireContext()).transaksiDao()

        // Load data transaksi saat halaman dibuat
        loadRiwayatTransaksi()

        return view
    }

    // Dipanggil setiap kali fragment kembali terlihat
    override fun onResume() {
        super.onResume()
        updateTransaksiList()
    }

    private fun updateTransaksiList() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Ambil semua data transaksi terbaru
            val transaksiList = transaksiDao.getAllTransaksi()

            withContext(Dispatchers.Main) {
                // Update RecyclerView dengan data baru
                transaksiAdapter = TransaksiAdapter(transaksiList)
                recyclerView.adapter = transaksiAdapter
            }
        }
    }

    private fun loadRiwayatTransaksi() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Ambil semua transaksi dari database
            val transaksiList = transaksiDao.getAllTransaksi()

            withContext(Dispatchers.Main) {
                // Set adapter untuk RecyclerView
                transaksiAdapter = TransaksiAdapter(transaksiList)
                recyclerView.adapter = transaksiAdapter
            }
        }
    }
}
