package com.example.cafeapp.MakanDatabase

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.Makan
import com.example.cafeapp.databinding.ActivityTestDatabase2Binding

class TestDatabase2 : AppCompatActivity() {
    private lateinit var binding: ActivityTestDatabase2Binding
    private val menuViewModel: MakanViewModel by viewModels() // Inisialisasi ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestDatabase2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Observing data from ViewModel
        menuViewModel.getAllMakans().observe(this, Observer { menus ->
            // Perbarui adapter ketika data berubah
            binding.recyclerView.adapter = MakanAdapter(menus) { menu ->
                DialogKonfirmasi(menu)
            }
        })

        binding.back.setOnClickListener(){
            val intent = Intent(this, TestDatabase1::class.java)
            startActivity(intent)
        }
    }

    private fun DialogKonfirmasi(menu: Makan) {
        // Dialog konfirmasi untuk menghapus item menu
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Are you sure you want to delete ${menu.name}?")

        builder.setPositiveButton("Yes") { _, _ ->
            menuViewModel.deleteMakanById(menu._id) // Menghapus menu menggunakan ViewModel
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Tutup dialog jika pengguna membatalkan
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}