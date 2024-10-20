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
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.MakanDatabase.MakanViewModel
import com.example.cafeapp.MinumDatabase.Minum
import com.example.cafeapp.MinumDatabase.MinumViewModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityTestDatabase2Binding
import java.io.File

class ListDataMenu : AppCompatActivity() {
    private lateinit var binding: ActivityTestDatabase2Binding
    private val makanViewModel: MakanViewModel by viewModels()
    private val minumViewModel: MinumViewModel by viewModels()// Inisialisasi ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestDatabase2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView Makanan
        binding.recyclerView1.layoutManager = LinearLayoutManager(this)

        // Observing data from ViewModel Makanan

        makanViewModel.getAllMakans().observe(this) { menus ->
            // Perbarui adapter ketika data berubah
            binding.recyclerView1.adapter = MakanAdminAdapter(menus, object :
                MakanAdminAdapter.OnItemClickListener {
                override fun onEditClick(menu: Makan) {
                    showEditDialogMakan(menu) // Panggil fungsi edit dialog
                }

                override fun onDeleteClick(menu: Makan) {
                    DialogKonfirmasi(menu, "makans") // Panggil fungsi konfirmasi hapus
                }
            })
        }

        // Setup RecyclerView Minuman
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)

        // Observing data from ViewModel Minuman
        minumViewModel.getAllMinums().observe(this) { menus ->
            // Perbarui adapter ketika data berubah
            binding.recyclerView2.adapter = MinumAdminAdapter(menus, object :
                MinumAdminAdapter.OnItemClickListener {
                override fun onEditClick(menu: Minum) {
                    showEditDialogMinum(menu) // Panggil fungsi edit dialog
                }

                override fun onDeleteClick(menu: Minum) {
                    DialogKonfirmasi(menu, "minums") // Panggil fungsi konfirmasi hapus
                }
            })
        }

        binding.btnBack.setOnClickListener(){
            finish()
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
        builder.setTitle("Pilih Tindakan")
        builder.setMessage("Pilih tindakan yang akan diambil ${itemName}?")

        builder.setPositiveButton("Ya") { _, _ ->
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

        builder.setNegativeButton("Tidak") { dialog, _ ->

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showEditDialogMakan(item: Makan) {

        // Inflate layout dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_edit_data, null)

        val editNama = dialogView.findViewById<EditText>(R.id.editNama)
        val editHarga = dialogView.findViewById<EditText>(R.id.editHarga)
        val editDeskripsi = dialogView.findViewById<EditText>(R.id.editDeskripsi)
        val editFoto = dialogView.findViewById<ImageView>(R.id.editFoto)

        // Set nilai awal data
        editNama.setText(item.name)
        editHarga.setText(item.harga.toString())
        editDeskripsi.setText(item.deskripsi)

        // Tampilkan foto jika ada
        val imgPath = File(filesDir, "app_images/${item.namaFoto}")
        if (imgPath.exists()) {
            editFoto.setImageURI(Uri.fromFile(imgPath))
        }

        // Buat dialog
        AlertDialog.Builder(this)
            .setTitle("Edit Item")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                // Ambil inputan dari pengguna
                val updatedName = editNama.text.toString()
                val updatedHarga = editHarga.text.toString().toDouble()
                val updatedDeskripsi = editDeskripsi.text.toString()

                // Lakukan update ke ViewModel
                makanViewModel.updateMakan(
                    id = item._id, // ID dari item Makan
                    name = updatedName, // Nama baru
                    harga = updatedHarga, // Harga baru
                    deskripsi = updatedDeskripsi, // Deskripsi baru
                    namaFoto = item.namaFoto // Nama foto tetap sama (jika tidak diubah)
                )

                // Jika perlu, panggil notifyDataSetChanged() di adapter
            }
            .setNegativeButton("Batal", null)
            .show()
    }
    private fun showEditDialogMinum(item: Minum) {

        // Inflate layout dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_edit_data, null)

        val editNama = dialogView.findViewById<EditText>(R.id.editNama)
        val editHarga = dialogView.findViewById<EditText>(R.id.editHarga)
        val editDeskripsi = dialogView.findViewById<EditText>(R.id.editDeskripsi)
        val editFoto = dialogView.findViewById<ImageView>(R.id.editFoto)

        // Set nilai awal data
        editNama.setText(item.name)
        editHarga.setText(item.harga.toString())
        editDeskripsi.setText(item.deskripsi)

        // Tampilkan foto jika ada
        val imgPath = File(filesDir, "app_images/${item.namaFoto}")
        if (imgPath.exists()) {
            editFoto.setImageURI(Uri.fromFile(imgPath))
        }

        // Buat dialog
        AlertDialog.Builder(this)
            .setTitle("Edit Item")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                // Ambil inputan dari pengguna
                val updatedName = editNama.text.toString()
                val updatedHarga = editHarga.text.toString().toDouble()
                val updatedDeskripsi = editDeskripsi.text.toString()

                // Lakukan update ke ViewModel
                minumViewModel.updateMinum(
                    id = item._id, // ID dari item Makan
                    name = updatedName, // Nama baru
                    harga = updatedHarga, // Harga baru
                    deskripsi = updatedDeskripsi, // Deskripsi baru
                    namaFoto = item.namaFoto // Nama foto tetap sama (jika tidak diubah)
                )

                // Jika perlu, panggil notifyDataSetChanged() di adapter
            }
            .setNegativeButton("Batal", null)
            .show()
    }


}