package com.example.cafeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.CafeDatabase
import com.example.cafeapp.MenuDatabase.MenuDAO
import com.example.cafeapp.Transaksi.RiwayatTransaksiFragment
import com.example.cafeapp.Transaksi.Transaksi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class TransaksiFragment : Fragment() {

    private lateinit var db: CafeDatabase
    private lateinit var menuDao: MenuDAO
    private var selectedMenuId: Int? = null  // Untuk menyimpan ID menu yang dipilih

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaksi, container, false)

        // Inisialisasi database dan DAO
        db = CafeDatabase.getInstance(requireContext())
        menuDao = db.menuDao()

        // Menyiapkan komponen UI
        val editTextJumlah = view.findViewById<EditText>(R.id.etJumlahPesanan)
        val editTextPembayaran = view.findViewById<EditText>(R.id.etPembayaran)
        val buttonTransaksi = view.findViewById<Button>(R.id.buttonTransaksi)
        val spinnerMenu = view.findViewById<Spinner>(R.id.spinnerMenu)
        val textViewKembalian = view.findViewById<TextView>(R.id.textViewKembalian)
        val buttonRiwayatTransaksi = view.findViewById<Button>(R.id.buttonRiwayatTransaksi)


        // Setup Spinner untuk memilih menu
        setupSpinnerMenu(spinnerMenu)

        buttonTransaksi.setOnClickListener {
            val jumlahTransaksi = editTextJumlah.text.toString().toIntOrNull() ?: 0
            val totalPembayaran = editTextPembayaran.text.toString().toIntOrNull() ?: 0

            if (jumlahTransaksi <= 0 || totalPembayaran <= 0) {
                Toast.makeText(requireContext(), "Jumlah pesanan dan pembayaran harus diisi dengan benar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val menuId = selectedMenuId
            if (menuId == null) {
                Toast.makeText(requireContext(), "Silakan pilih menu terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                // Ambil data menu berdasarkan ID yang dipilih
                val menu = menuDao.getMakanById(menuId).value

                if (menu != null) {
                    val hargaPerItem = menu.harga
                    val totalHarga = hargaPerItem * jumlahTransaksi
                    val kembalian = totalPembayaran - totalHarga

                    if (totalPembayaran >= totalHarga) {
                        // Simpan transaksi ke database
                        val transaksi = Transaksi(
                            id = 0,  // ID akan auto-generate
                            namaMenu = menu.nama,
                            harga = totalHarga,
                            pembayaran = totalPembayaran,
                            kembalian = kembalian,
                            tanggalTransaksi = getCurrentDate(),
                            jamTransaksi = getCurrentTime(),
                            jumlahTransaksi = jumlahTransaksi
                        )

                        // Insert transaksi ke database
                        db.transaksiDao().insertTransaksi(transaksi)

                        // Tampilkan kembalian di UI
                        withContext(Dispatchers.Main) {
                            textViewKembalian.text = "Kembalian: Rp${kembalian}"
                            textViewKembalian.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), "Transaksi berhasil!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Pembayaran kurang
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Pembayaran kurang!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Menu tidak ditemukan
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Menu tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        buttonRiwayatTransaksi.setOnClickListener {
            // Pindah ke RiwayatTransaksiFragment dengan FragmentTransaction
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, RiwayatTransaksiFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        return view
    }

    // Setup Spinner untuk memilih menu
    private fun setupSpinnerMenu(spinnerMenu: Spinner) {
        lifecycleScope.launch(Dispatchers.IO) {
            // Ambil semua data menu dari database
            val menuList = menuDao.getAllData()
            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, menuList.map { it.nama })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerMenu.adapter = adapter

                spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        // Ambil ID menu yang dipilih
                        selectedMenuId = menuList[position]._id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Tidak ada pilihan menu
                        selectedMenuId = null
                    }
                }
            }
        }
    }

    // Mendapatkan tanggal sekarang
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    // Mendapatkan waktu sekarang
    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(Date())
    }
}
