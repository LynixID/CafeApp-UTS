package com.example.cafeapp.AdminMenu

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityTestDatabase2Binding
import com.example.cafeapp.databinding.ModalEditDataBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import java.io.IOException

class ListDataMenu : AppCompatActivity() {
    private lateinit var binding: ActivityTestDatabase2Binding
    private val menuViewModel: MenuViewModel by viewModels()
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    private var selectedImageName: String? = null
    private val firebaseDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("list menu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Konfigurasi launcher untuk memilih gambar
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    val bitmap = getBitmapFromUri(this, uri)
                    if (bitmap != null) {
                        selectedImageName = uri.lastPathSegment // Menggunakan nama file dari URI
                    }
                }
            }
        }

        // Mengatur tampilan utama
        binding = ActivityTestDatabase2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView1.layoutManager = LinearLayoutManager(this)

        syncFirebaseToLocalDatabase()

        // Observasi data menu dari database
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

    private fun showConfirmationDialog(item: Menu, table: String) {
        val itemName = item.nama

        AlertDialog.Builder(this)
            .setTitle("Hapus Menu")
            .setMessage("Apakah Anda yakin ingin menghapus menu '$itemName'?")
            .setPositiveButton("Ya") { _, _ ->
                if (table == "menus") {
                    // Hapus dari database lokal
                    menuViewModel.deleteMakanById(item._id)

                    // Hapus dari Firebase
                    firebaseDatabase.child(item._id.toString()).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Menu berhasil dihapus dari Firebase!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal menghapus dari Firebase.", Toast.LENGTH_SHORT).show()
                        }

                    Toast.makeText(this, "Menu berhasil dihapus.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Tidak", null)
            .create()
            .show()
    }

    private fun showEditDialogMakan(item: Menu) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_edit_data, null)
        val dialogBinding = ModalEditDataBinding.bind(dialogView)

        // Menampilkan data awal
        dialogBinding.editNama.setText(item.nama)
        dialogBinding.editHarga.setText(item.harga.toString())
        dialogBinding.editDeskripsi.setText(item.deskripsi)

        // Menampilkan gambar awal
        val imgPath = Uri.parse(item.namaFoto)
        dialogBinding.editFoto.setImageURI(imgPath)

        // Ganti gambar
        dialogBinding.editFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            imagePickerLauncher.launch(intent)
        }

        // Dialog untuk menyimpan perubahan
        AlertDialog.Builder(this)
            .setTitle("Edit Menu")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val updatedName = dialogBinding.editNama.text.toString().trim()
                val updatedHarga = dialogBinding.editHarga.text.toString().toIntOrNull() ?: 0
                val updatedDeskripsi = dialogBinding.editDeskripsi.text.toString().trim()

                if (updatedName.isEmpty() || updatedHarga <= 0 || updatedDeskripsi.isEmpty()) {
                    Toast.makeText(this, "Semua field harus diisi dengan benar!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val finalImageName = selectedImageName ?: item.namaFoto // Menggunakan gambar baru jika ada, atau gambar lama

                // Buat objek Menu baru dengan data yang diperbarui
                val updatedMenu = Menu(
                    _id = item._id,
                    nama = updatedName,
                    harga = updatedHarga,
                    deskripsi = updatedDeskripsi,
                    namaFoto = finalImageName,
                    kategori = item.kategori
                )

                // Simpan ke database lokal
                menuViewModel.updateMakan(
                    id = updatedMenu._id,
                    name = updatedMenu.nama,
                    harga = updatedMenu.harga,
                    deskripsi = updatedMenu.deskripsi,
                    namaFoto = updatedMenu.namaFoto
                )

                // Simpan ke Firebase
                firebaseDatabase.child(updatedMenu._id.toString()).setValue(updatedMenu)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Menu berhasil diperbarui di Firebase!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal memperbarui di Firebase.", Toast.LENGTH_SHORT).show()
                    }

                Toast.makeText(this, "Menu berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun syncFirebaseToLocalDatabase() {
        firebaseDatabase.get().addOnSuccessListener { snapshot ->
            val menus = snapshot.children.mapNotNull { it.getValue(Menu::class.java) }
            menus.forEach { menu ->
                menuViewModel.insertMakan(menu) // Sinkronkan ke database lokal
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal sinkronisasi dari Firebase.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBitmapFromUri(context: ListDataMenu, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
