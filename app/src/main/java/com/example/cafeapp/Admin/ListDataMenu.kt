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
    private val minumViewModel: MinumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestDatabase2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        observeViewModels()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerViews() {
        binding.recyclerView1.layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModels() {
        makanViewModel.getAllMakans().observe(this) { menus ->
            binding.recyclerView1.adapter = MakanAdminAdapter(menus, object : MakanAdminAdapter.OnItemClickListener {
                override fun onEditClick(menu: Makan) {
                    showEditDialogMakan(menu)
                }

                override fun onDeleteClick(menu: Makan) {
                    showConfirmationDialog(menu, "makans")
                }
            })
        }

        minumViewModel.getAllMinums().observe(this) { menus ->
            binding.recyclerView2.adapter = MinumAdminAdapter(menus, object : MinumAdminAdapter.OnItemClickListener {
                override fun onEditClick(menu: Minum) {
                    showEditDialogMinum(menu)
                }

                override fun onDeleteClick(menu: Minum) {
                    showConfirmationDialog(menu, "minums")
                }
            })
        }
    }

    private fun showConfirmationDialog(item: Any, tabel: String) {
        val itemName = when (item) {
            is Makan -> item.name
            is Minum -> item.name
            else -> "Unknown"
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Tindakan")
            .setMessage("Pilih tindakan yang akan diambil $itemName?")
            .setPositiveButton("Ya") { _, _ ->
                when (tabel) {
                    "makans" -> if (item is Makan) {
                        makanViewModel.deleteMakanById(item._id)
                    }
                    "minums" -> if (item is Minum) {
                        minumViewModel.deleteMinumById(item._id)
                    }
                }
            }
            .setNegativeButton("Tidak", null)
            .create()
            .show()
    }

    private fun showEditDialogMakan(item: Makan) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_edit_data, null)
        val editNama = dialogView.findViewById<EditText>(R.id.editNama)
        val editHarga = dialogView.findViewById<EditText>(R.id.editHarga)
        val editDeskripsi = dialogView.findViewById<EditText>(R.id.editDeskripsi)
        val editFoto = dialogView.findViewById<ImageView>(R.id.editFoto)

        editNama.setText(item.name)
        editHarga.setText(item.harga.toString())
        editDeskripsi.setText(item.deskripsi)

        val imgPath = File(filesDir, "app_images/${item.namaFoto}")
        if (imgPath.exists()) {
            editFoto.setImageURI(Uri.fromFile(imgPath))
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Item")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val updatedName = editNama.text.toString()
                val updatedHarga = editHarga.text.toString().toDouble()
                val updatedDeskripsi = editDeskripsi.text.toString()

                makanViewModel.updateMakan(
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

    private fun showEditDialogMinum(item: Minum) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_edit_data, null)
        val editNama = dialogView.findViewById<EditText>(R.id.editNama)
        val editHarga = dialogView.findViewById<EditText>(R.id.editHarga)
        val editDeskripsi = dialogView.findViewById<EditText>(R.id.editDeskripsi)
        val editFoto = dialogView.findViewById<ImageView>(R.id.editFoto)

        editNama.setText(item.name)
        editHarga.setText(item.harga.toString())
        editDeskripsi.setText(item.deskripsi)

        val imgPath = File(filesDir, "app_images/${item.namaFoto}")
        if (imgPath.exists()) {
            editFoto.setImageURI(Uri.fromFile(imgPath))
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Item")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val updatedName = editNama.text.toString()
                val updatedHarga = editHarga.text.toString().toDouble()
                val updatedDeskripsi = editDeskripsi.text.toString()

                minumViewModel.updateMinum(
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
