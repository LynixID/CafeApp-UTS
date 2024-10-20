package com.example.cafeapp.TambahMenu

import com.example.cafeapp.MakanDatabase.MakanViewModel
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
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.MakanDatabase.TestDatabase2
import com.example.cafeapp.MinumDatabase.Minum
import com.example.cafeapp.MinumDatabase.MinumViewModel
//import com.example.cafeapp.MinumDatabase.MinumViewModel
//import com.example.cafeapp.MinumanDatabase.MinumanViewModel
//import com.example.cafeapp.SnackDatabase.SnackViewModel
//import com.example.cafeapp.MinumanDatabase.Minuman
//import com.example.cafeapp.SnackDatabase.Snack
import com.example.cafeapp.databinding.TambahMenuBinding
import java.io.IOException

class TambahMenu : AppCompatActivity() {

    private lateinit var binding: TambahMenuBinding
    private val makanViewModel: MakanViewModel by viewModels()
    private val minumViewModel: MinumViewModel by viewModels()
//    private val snackViewModel: SnackViewModel by viewModels()
    private var imagePath: String? = null

    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TambahMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Clear input fields
        clearInputFields()

        // Set up Spinner for category selection
        val kategoriList = arrayOf("Makanan", "Minuman", "Snack")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kategoriList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = adapter

        // Image selection logic
        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.foto.setImageURI(uri) // Show selected image
                    binding.foto.visibility = View.VISIBLE // Make ImageView visible

                    // Convert URI to Bitmap
                    val bitmap = getBitmapFromUri(this, uri)
                    if (bitmap != null) {
                        val imageName = "menu_${System.currentTimeMillis()}" // Unique file name
                        makanViewModel.saveImageToInternalStorage(bitmap, imageName)?.let { savedImageName ->
                            imagePath = savedImageName // Save image file name
                        }
                    }
                }
            }
        }

        // Button to select image
        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getImageLauncher.launch(intent)
        }

        // Submit button logic
        binding.btnTambahMenu.setOnClickListener {
            addMenu()

        }

        binding.btnNext.setOnClickListener(){
            val intent = Intent(this, TestDatabase2::class.java)
            startActivity(intent)
        }
    }

    private fun addMenu() {
        val nama = binding.inputNamaProduk.text.toString()
        val harga = binding.inputHargaProduk.text.toString().toIntOrNull() ?: 0
        val deskripsi = binding.inputDeskripsiProduk.text.toString()
        val kategori = binding.spinnerKategori.selectedItem.toString() // Get selected category

        if (nama.isNotEmpty() && harga >= 0 && imagePath != null) {
            when (kategori) {
                "Makanan" -> {
                    // Create Makan object and save to Makan table
                    val makan = Makan(
                        _id = 0,
                        name = nama,
                        harga = harga,
                        deskripsi = deskripsi,
                        namaFoto = imagePath!!
                    )
                    makanViewModel.insertMakan(makan)
                }
                "Minuman" -> {
                    // Create Minuman object and save to Minuman table
                    val minuman = Minum(
                        _id = 0,
                        name = nama,
                        harga = harga,
                        deskripsi = deskripsi,
                        namaFoto = imagePath!!
                    )
                    minumViewModel.insertMinum(minuman)
                }
            }

            Toast.makeText(this, "Menu berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            val intent= Intent(this, TestDatabase2::class.java)
            startActivity(intent)

            // Clear input fields after saving
            clearInputFields()
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

    // Function to convert URI to Bitmap
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
}
