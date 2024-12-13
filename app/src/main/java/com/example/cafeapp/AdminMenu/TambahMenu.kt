package com.example.cafeapp.AdminMenu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.cafeapp.Login.Login_page
import com.example.cafeapp.MenuDatabase.Kategori
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.TambahMenuBinding
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

class TambahMenu : AppCompatActivity() {
    // Deklarasi variabel yang digunakan
    private lateinit var binding: TambahMenuBinding
    private val menuViewModel: MenuViewModel by viewModels()
    private var imagePath: String? = null
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val prefsName = "GalleryPermissionPrefs"
    private val keyPermission = "gallery_permission"
    private val database = FirebaseDatabase.getInstance()
    private val menuRef = database.getReference("menu")

    // Fungsi onCreate untuk inisialisasi dan pengaturan awal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TambahMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur input awal
        clearInputFields()

        // Mengatur spinner untuk memilih kategori
        val kategoriList = arrayOf("Makanan", "Minuman")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kategoriList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = adapter

        // Logika untuk memilih gambar
        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Menggunakan Glide untuk menampilkan gambar
                    Glide.with(this)
                        .load(uri)  // Memuat gambar dari URI yang dipilih
                        .placeholder(R.drawable.placeholder_image)  // Placeholder
                        .into(binding.foto)  // Menampilkan gambar pada ImageView
                    binding.foto.visibility = View.VISIBLE // Membuat ImageView terlihat

                    // Konversi URI ke Bitmap
                    val bitmap = getBitmapFromUri(this, uri)
                    if (bitmap != null) {
                        val imageName = "menu_${System.currentTimeMillis()}" // Nama file unik
                        menuViewModel.saveImageToInternalStorage(bitmap, imageName)?.let { savedImageName ->
                            imagePath = savedImageName // Simpan nama file gambar
                        }
                    }
                }
            }
        }

        // Mendaftarkan izin akses galeri
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showPermissionChoiceDialog()
            } else {
                showPermissionRationaleDialog()
            }
        }

        // Tombol untuk memeriksa izin dan memilih gambar
        binding.btnSelectImage.setOnClickListener {
            checkAndRequestPermission()
        }

        // Tombol untuk menambahkan menu
        binding.btnTambahMenu.setOnClickListener {
            addMenu()
        }

        // Tombol untuk kembali ke halaman sebelumnya
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, Login_page::class.java)
            startActivity(intent)
        }

        // Tombol untuk melanjutkan ke daftar menu
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, ListDataMenu::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk memeriksa dan meminta izin
    private fun checkAndRequestPermission() {
        val permissionState = getPermissionState()
        if (permissionState == "always") {
            selectImage()
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED) {
            selectImage()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    // Menampilkan dialog untuk memilih jenis izin
    private fun showPermissionChoiceDialog() {
        AlertDialog.Builder(this)
            .setTitle("Pilih Opsi Izin")
            .setMessage("Apakah Anda ingin memberikan izin akses galeri untuk saat ini atau selalu?")
            .setPositiveButton("Izinkan Selalu") { dialog, _ ->
                savePermissionState("always")
                selectImage()
                dialog.dismiss()
            }
            .setNegativeButton("Izinkan Hanya Sekali") { dialog, _ ->
                savePermissionState("once")
                selectImage()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    // Menampilkan dialog jika izin tidak diberikan
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Izin Akses Galeri Diperlukan")
            .setMessage("Aplikasi ini memerlukan izin akses galeri untuk memilih gambar. Apakah Anda ingin memberikan izin?")
            .setPositiveButton("Izinkan") { dialog, _ ->
                dialog.dismiss()
                showPermissionChoiceDialog()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Izin akses galeri diperlukan untuk menambahkan gambar.", Toast.LENGTH_SHORT).show()
            }
            .setCancelable(false)
            .show()
    }

    // Fungsi untuk membuka galeri dan memilih gambar
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getImageLauncher.launch(intent)
    }

    // Fungsi untuk menambahkan menu baru
    private fun addMenu() {
        val nama = binding.inputNamaProduk.text.toString()
        val harga = binding.inputHargaProduk.text.toString().toIntOrNull() ?: 0
        val deskripsi = binding.inputDeskripsiProduk.text.toString()
        val kategori = binding.spinnerKategori.selectedItem.toString()

        if (nama.isNotEmpty() && harga >= 0 && imagePath != null) {
            val categoryEnum = when (kategori) {
                "Makanan" -> Kategori.MAKAN
                "Minuman" -> Kategori.MINUM
                else -> null
            }

            if (categoryEnum != null) {
                val menu = Menu(
                    nama = nama,
                    harga = harga,
                    deskripsi = deskripsi,
                    kategori = categoryEnum,
                    namaFoto = imagePath!!
                )
                menuViewModel.insertMakan(menu)

                val menuId = menuRef.push().key ?: return
                menuRef.child(menuId).setValue(menu)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Menu berhasil disimpan di Firebase", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ListDataMenu::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menyimpan ke Firebase: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                clearInputFields()
            } else {
                Toast.makeText(this, "Kategori tidak valid", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk membersihkan input setelah data disimpan
    private fun clearInputFields() {
        binding.inputNamaProduk.text?.clear()
        binding.inputHargaProduk.text?.clear()
        binding.inputDeskripsiProduk.text?.clear()
        binding.foto.setImageURI(null)
        binding.foto.visibility = View.GONE
        binding.spinnerKategori.setSelection(0)
    }

    // Fungsi untuk mengonversi URI ke Bitmap
    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
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

    // Fungsi untuk menyimpan status izin
    private fun savePermissionState(state: String) {
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefs.edit().putString(keyPermission, state).apply()
    }

    // Fungsi untuk mendapatkan status izin
    private fun getPermissionState(): String? {
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.getString(keyPermission, "once")
    }
}