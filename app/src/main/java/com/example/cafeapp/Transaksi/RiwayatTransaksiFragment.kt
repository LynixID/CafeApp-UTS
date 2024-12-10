package com.example.cafeapp.Transaksi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.UserDatabase.CafeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class RiwayatTransaksiFragment : Fragment() {
    private lateinit var db: TransaksiDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransaksiAdapter
    private var isNewestFirst = true  // Default is Terbaru

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_riwayat_transaksi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = TransaksiDatabase.getInstance(requireContext())
        recyclerView = view.findViewById(R.id.recyclerViewRiwayatTransaksi)

        // Setup RecyclerView
        setupRecyclerView()

        // Setup Back Button
        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            findNavController().navigate(R.id.action_RiwayatTransaksiFragment_to_TransaksiFragment)
        }


        // Setup Sort Spinner
        setupSortSpinner(view)

        // Load initial data
        loadTransactions()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupSortSpinner(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.sortSpinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                isNewestFirst = position == 0  // Position 0 is for "Terbaru"
                loadTransactions()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak perlu aksi khusus jika tidak ada yang dipilih
            }
        }
    }

    private fun loadTransactions() {
        lifecycleScope.launch(Dispatchers.IO) {
            val transaksiList = db.transaksiDao().getAllTransaksi()

            val sortedList = if (isNewestFirst) {
                // Untuk yang terbaru di atas, gunakan sortedByDescending
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
                // Untuk yang terlama di atas, gunakan sortedBy
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

            withContext(Dispatchers.Main) {
                if (sortedList.isEmpty()) {
                    Toast.makeText(requireContext(), "Belum ada transaksi", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    adapter = TransaksiAdapter(sortedList)
                    recyclerView.adapter = adapter
                }
            }
        }
    }
}
