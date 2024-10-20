package com.example.cafeapp.TambahMenu

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.MakanDatabase.MakanAdapter
import com.example.cafeapp.MakanDatabase.MakanViewModel
import com.example.cafeapp.MinumDatabase.Minum
import com.example.cafeapp.MinumDatabase.MinumAdapter
import com.example.cafeapp.MinumDatabase.MinumViewModel
import com.example.cafeapp.databinding.ActivityTestDatabase2Binding
import com.example.cafeapp.databinding.TestAllMenuBinding
import com.example.cafeapp.databinding.TestItemMenuBinding

class MakanItemsActivity : AppCompatActivity() {
    private lateinit var binding: TestAllMenuBinding
    private val makanViewModel: MakanViewModel by viewModels()
    private val minumViewModel: MinumViewModel by viewModels()// Inisialisasi ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = TestAllMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView Makanan
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Observing data from ViewModel Makanan
        makanViewModel.getAllMakans().observe(this, Observer { menus ->
            // Perbarui adapter ketika data berubah
            binding.recyclerView.adapter = MakanAdapter(menus) { menu ->
                DialogKonfirmasi(menu, "makans")
            }
        })


        binding.btnBack.setOnClickListener(){
            val intent = Intent(this, TambahMenu::class.java)
            startActivity(intent)
        }
        binding.btnNext.setOnClickListener(){
            val intent = Intent(this, TambahMenu::class.java)
            startActivity(intent)
        }
    }

    private fun DialogKonfirmasi(item: Any, tabel: String) {
        // Tentukan nama item berdasarkan tipe data
        val itemName = when(item) {
            is Makan -> item.name
            is Minum -> item.name
            else -> "Unknown"
        }

        // Dialog konfirmasi untuk menghapus item menu
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Are you sure you want to delete ${itemName}?")

        builder.setPositiveButton("Yes") { _, _ ->
            when (tabel) {
                "makans" -> {
                    if (item is Makan) {
                        makanViewModel.deleteMakanById(item._id) // Hapus dari tabel Makan
                    }
                }
                "minums" -> {
                    if (item is Minum) {
                        minumViewModel.deleteMinumById(item._id) // Hapus dari tabel Minum
                    }
                }
            }
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Tutup dialog jika pengguna membatalkan
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}