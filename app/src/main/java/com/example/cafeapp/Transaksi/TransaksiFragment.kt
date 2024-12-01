package com.example.cafeapp.Transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.MenuDatabase.MenuAdapter
import com.example.cafeapp.UserDatabase.CafeDatabase
import com.example.cafeapp.MenuDatabase.MenuDAO
import com.example.cafeapp.R
import com.example.cafeapp.databinding.FragmentTransaksiBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

// Fungsi observeOnce untuk mengamati LiveData hanya sekali
fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
    observeForever(object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer.onChanged(value)
        }
    })
}

class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: CafeDatabase
    private lateinit var menuDao: MenuDAO
    private var selectedMenuId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)

        // Inisialisasi database dan menuDao
        db = CafeDatabase.getInstance(requireContext())  // Mengambil instance dari CafeDatabase
        menuDao = db.menuDao()  // Mengambil menuDao dari instance CafeDatabase

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalPrice = arguments?.getDouble("total_price") ?: 0.0

        // Tampilkan total harga menggunakan binding
        binding.tvTotalLabel.text = "Total: Rp${String.format("%,.2f", totalPrice)}"

        // Setup daftar menu
        setupRecyclerView()

        // Setup aksi transaksi selesai
        setupCompleteTransaction(totalPrice)
    }

    private fun setupCompleteTransaction(totalPrice: Double) {
        binding.btnCompleteTransaction.setOnClickListener {
            val pembayaran = binding.etPembayaran.text.toString().toDoubleOrNull() ?: 0.0
            val kembalian = pembayaran - totalPrice

            if (pembayaran >= totalPrice) {
                // Simpan transaksi ke database
                lifecycleScope.launch(Dispatchers.IO) {
                    val transaksi = Transaksi(
                        id = 0,
                        namaMenu = "Total Order", // Atur sesuai kebutuhan
                        pembayaran = pembayaran.toInt(),
                        jumlahTransaksi = totalPrice.toInt(),
                        waktu = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    )
                    db.transaksiDao().insertTransaksi(transaksi)

                    // Navigasi ke RiwayatTransaksiFragment
                    withContext(Dispatchers.Main) {
                        binding.textViewKembalian.text = "Kembalian: Rp${String.format("%,.2f", kembalian)}"
                        findNavController().navigate(R.id.action_transaksiFragment_to_riwayatTransaksiFragment)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Pembayaran kurang!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        lifecycleScope.launch(Dispatchers.IO) {
            val menuList = menuDao.getAllData() // Mengambil data dari menuDao yang sudah diinisialisasi
            withContext(Dispatchers.Main) {
                val adapter = MenuAdapter(menuList) { selectedMenu ->
                    selectedMenuId = selectedMenu._id
                    Toast.makeText(requireContext(), "Menu dipilih: ${selectedMenu.nama}", Toast.LENGTH_SHORT).show()
                }
                binding.rvMenuList.layoutManager = LinearLayoutManager(requireContext())
                binding.rvMenuList.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
