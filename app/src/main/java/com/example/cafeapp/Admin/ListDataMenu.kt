package com.example.cafeapp.Admin

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityTestDatabase2Binding
import java.io.File

class ListDataMenu : AppCompatActivity() {
    private lateinit var binding: ActivityTestDatabase2Binding
    private val menuViewModel: MenuViewModel by viewModels() // Jangan ulangi ViewModelProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestDatabase2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView1.layoutManager = LinearLayoutManager(this)

        // Observe LiveData dengan benar
        menuViewModel.getAllMakans().observe(this) { menus ->
            binding.recyclerView1.adapter = MenuAdminAdapter(menus, object : MenuAdminAdapter.OnItemClickListener {
                override fun onEditClick(menu: Menu) {
                    showEditDialogMakan(menu)
                }

                override fun onDeleteClick(menu: Menu) {
                    showConfirmationDialog(menu, "menus")
                }
            })
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showConfirmationDialog(item: Any, tabel: String) {
        val itemName = when (item) {
            is Menu -> item.nama
            else -> "Unknown"
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Tindakan")
            .setMessage("Pilih tindakan yang akan diambil $itemName?")
            .setPositiveButton("Ya") { _, _ ->
                when (tabel) {
                    "menus" -> if (item is Menu) {
                        menuViewModel.deleteMakanById(item._id)
                    }
                }
            }
            .setNegativeButton("Tidak", null)
            .create()
            .show()
    }

    private fun showEditDialogMakan(item: Menu) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_edit_data, null)

        // Use view binding for dialog views
        val dialogBinding = com.example.cafeapp.databinding.ModalEditDataBinding.bind(dialogView)

        // Menampilkan data item pada EditText dan ImageView
        dialogBinding.editNama.setText(item.nama)
        dialogBinding.editHarga.setText(item.harga.toString())
        dialogBinding.editDeskripsi.setText(item.deskripsi)

        val imgPath = File(filesDir, "app_images/${item.namaFoto}")
        if (imgPath.exists()) {
            dialogBinding.editFoto.setImageURI(Uri.fromFile(imgPath))
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Item")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val updatedName = dialogBinding.editNama.text.toString()
                val updatedHarga = dialogBinding.editHarga.text.toString().toInt()
                val updatedDeskripsi = dialogBinding.editDeskripsi.text.toString()

                // Update data menggunakan ViewModel
                menuViewModel.updateMakan(
                    id = item._id,
                    name = updatedName,
                    harga = updatedHarga,
                    deskripsi = updatedDeskripsi,
                    namaFoto = item.namaFoto
                )
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
