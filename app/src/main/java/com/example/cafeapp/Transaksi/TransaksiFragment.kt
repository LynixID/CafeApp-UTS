package com.example.cafeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.UserDatabase.CafeDatabase
import com.example.cafeapp.MenuDatabase.MenuDAO
import com.example.cafeapp.Transaksi.RiwayatTransaksiFragment
import com.example.cafeapp.Transaksi.Transaksi
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

    private lateinit var db: CafeDatabase
    private lateinit var menuDao: MenuDAO
    private var selectedMenuId: Int? = null

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

            lifecycleScope.launch {
                menuDao.getMakanById(menuId).observeOnce { menu ->
                    if (menu != null) {
                        val hargaPerItem = menu.harga
                        val totalHarga = hargaPerItem * jumlahTransaksi
                        val kembalian = totalPembayaran - totalHarga

                        if (totalPembayaran >= totalHarga) {
                            val transaksi = Transaksi(
                                id = 0,
                                namaMenu = menu.nama,
                                pembayaran = totalPembayaran,
                                jumlahTransaksi = jumlahTransaksi
                            )

                            lifecycleScope.launch(Dispatchers.IO) {
                                db.transaksiDao().insertTransaksi(transaksi)
                                withContext(Dispatchers.Main) {
                                    textViewKembalian.text = "Kembalian: Rp${kembalian}"
                                    textViewKembalian.visibility = View.VISIBLE
                                    Toast.makeText(requireContext(), "Transaksi berhasil!", Toast.LENGTH_SHORT).show()

                                    // Navigasi ke RiwayatTransaksiFragment setelah transaksi berhasil
                                    val fragmentTransaction = parentFragmentManager.beginTransaction()
                                    fragmentTransaction.replace(R.id.fragment_container, RiwayatTransaksiFragment())
                                    fragmentTransaction.addToBackStack(null)
                                    fragmentTransaction.commit()
                                }
                            }
                        } else {
                            Toast.makeText(requireContext(), "Pembayaran kurang!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Menu tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        buttonRiwayatTransaksi.setOnClickListener {
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
            val menuList = menuDao.getAllData()
            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, menuList.map { it.nama })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerMenu.adapter = adapter

                spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedMenuId = menuList[position]._id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        selectedMenuId = null
                    }
                }
            }
        }
    }
}
