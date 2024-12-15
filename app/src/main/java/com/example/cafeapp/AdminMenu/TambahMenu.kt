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
import com.example.cafeapp.Login.Login_page
import com.example.cafeapp.MenuDatabase.Kategori
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuViewModel
import com.example.cafeapp.databinding.TambahMenuBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException


class TambahMenu : AppCompatActivity() {

    private lateinit var binding: TambahMenuBinding
    private val menuViewModel: MenuViewModel by viewModels()
    private var imagePath: String? = null
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val prefsName = "GalleryPermissionPrefs"
    private val keyPermission = "gallery_permission"
    private val database = FirebaseDatabase.getInstance()
    private val menuRef = database.getReference("menu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TambahMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Clear input fields
        clearInputFields()

        // Inisialisasi Firebase
        FirebaseApp.initializeApp(this)

        // Set up Spinner for category selection
        val kategoriList = arrayOf("Makanan", "Minuman")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kategoriList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = adapter

//        val storage: FirebaseStorage = FirebaseStorage.getInstance()
//        storage.useEmulator(
//            "127.0.0.1",
//            9199
//        )

        // Image selection logic
        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.foto.setImageURI(uri) // Show selected image
                    binding.foto.visibility = View.VISIBLE // Make ImageView visible
                    imagePath = uri.toString() // Save the image URI as the image path
                }
            }
        }

        // Register activity result for permission request
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showPermissionChoiceDialog() // Menampilkan dialog pilihan izin setelah diizinkan
            } else {
                showPermissionRationaleDialog()
            }
        }

        // Button to select image with permission check
        binding.btnSelectImage.setOnClickListener {
            checkAndRequestPermission()
        }

        // Submit button logic
        binding.btnTambahMenu.setOnClickListener {
            addMenu()
        }

        binding.btnBack.setOnClickListener(){
            val intent = Intent(this, Login_page::class.java)
            startActivity(intent)
        }

        binding.btnNext.setOnClickListener(){
            val intent = Intent(this, ListDataMenu::class.java)
            startActivity(intent)
        }
    }

    private fun checkAndRequestPermission() {
        val permissionState = getPermissionState()
        if (permissionState == "always") {
            // Jika izin selalu diberikan, langsung buka galeri
            selectImage()
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // Jika izin diberikan hanya untuk saat ini, langsung buka galeri
            selectImage()
        } else {
            // Minta izin akses galeri
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun showPermissionChoiceDialog() {
        AlertDialog.Builder(this)
            .setTitle("Pilih Opsi Izin")
            .setMessage("Apakah Anda ingin memberikan izin akses galeri untuk saat ini atau selalu?")
            .setPositiveButton("Izinkan Selalu") { dialog, _ ->
                savePermissionState("always") // Simpan izin sebagai selalu
                selectImage()
                dialog.dismiss()
            }
            .setNegativeButton("Izinkan Hanya Sekali") { dialog, _ ->
                savePermissionState("once") // Simpan izin sebagai sementara
                selectImage()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

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

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getImageLauncher.launch(intent)
    }

    private fun keLoginPage() {
        val intent = Intent(this, Login_page::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun addMenu() {
        val nama = binding.inputNamaProduk.text.toString()
        val harga = binding.inputHargaProduk.text.toString().toIntOrNull() ?: 0
        val deskripsi = binding.inputDeskripsiProduk.text.toString()
        val kategori = binding.spinnerKategori.selectedItem.toString()
        if (nama.isNotEmpty() && harga >= 0 && imagePath != null) {
            // Convert kategori from String to Category enum
            val categoryEnum = when (kategori) {
                "Makanan" -> Kategori.MAKAN
                "Minuman" -> Kategori.MINUM
                else -> null
            }

            if (categoryEnum != null) {
                val menu = Menu(
                    _id = 0,
                    nama = nama,
                    harga = harga,
                    deskripsi = deskripsi,
                    kategori = categoryEnum,
                    namaFoto = imagePath!!
                )
                menuViewModel.insertMakan(menu)

                // Simpan ke Firebase
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

                // Reset input field
                clearInputFields()
            } else {
                Toast.makeText(this, "Kategori tidak valid", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputFields() {
        binding.inputNamaProduk.text?.clear()
        binding.inputHargaProduk.text?.clear()
        binding.inputDeskripsiProduk.text?.clear()
        binding.foto.setImageURI(null)
        binding.foto.visibility = View.GONE
        binding.spinnerKategori.setSelection(0) // Reset spinner to first item
    }

    private fun savePermissionState(state: String) {
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefs.edit().putString(keyPermission, state).apply()
    }

    private fun getPermissionState(): String? {
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.getString(keyPermission, "once")
    }
}
