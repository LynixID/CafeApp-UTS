package com.example.cafeapp.Transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.UserDatabase.CafeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RiwayatTransaksiFragment : Fragment() {
    private lateinit var db: CafeDatabase
    private lateinit var transaksiAdapter: TransaksiAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewRiwayatTransaksi)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            val transaksiList = db.transaksiDao().getAllTransaksi()
            withContext(Dispatchers.Main) {
                val adapter = TransaksiAdapter(transaksiList)
                recyclerView.adapter = adapter
            }
        }
    }
}