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
import com.example.cafeapp.R
import com.example.cafeapp.databinding.TambahMenuBinding
import java.io.File
import java.io.IOException

class TambahMenu : AppCompatActivity() {

    private lateinit var binding: TambahMenuBinding
    private val makanViewModel: MakanViewModel by viewModels()
    private var imagePath: String? = null
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TambahMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clearInputFields()

        // Set up Spinner for category selection
        val kategoriList = arrayOf("Makanan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kategoriList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = adapter

        // Image selection logic
        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.foto.setImageURI(uri) // Tampilkan gambar yang dipilih
                    binding.foto.visibility = View.VISIBLE // Tampilkan ImageView

                    // Ubah URI menjadi Bitmap
                    val bitmap = getBitmapFromUri(this, uri)
                    if (bitmap != null) {
                        val imageName = "makan_${System.currentTimeMillis()}.png" // Nama file unik
                        makanViewModel.saveImageToInternalStorage(bitmap, imageName)?.let { savedImageName ->
                            imagePath = savedImageName // Simpan nama file gambar
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
    }

    private fun addMenu() {
        val nama = binding.inputNamaProduk.text.toString()
        val harga = binding.inputHargaProduk.text.toString().toIntOrNull() ?: 0
        val deskripsi = binding.inputDeskripsiProduk.text.toString()
        val kategori = binding.spinnerKategori.selectedItem.toString()

        // Debugging: Print values to log
        println("Nama: $nama, Harga: $harga, Deskripsi: $deskripsi, Kategori: $kategori, ImagePath: $imagePath")

        if (nama.isNotEmpty() && harga > 0 && imagePath != null) {
            val makan = Makan(
                _id = 0,
                name = nama,
                harga = harga,
                desk = deskripsi,
                kategori = kategori,
                imagePath = imagePath!!
            )

            // Insert sesuai dengan kategori
            when (kategori) {
                "Makanan" -> {
                    makanViewModel.insertMakan(makan)
                    println("Makan item inserted") // Debugging: Confirm insertion
                }
            }

            Toast.makeText(this, "Menu berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            clearInputFields()
        } else {
            Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
        }
    }

    // Menambahkan observer dalam activity, gunakan 'this' untuk LifecycleOwner
    override fun onStart() {
        super.onStart()
        makanViewModel.allMakans.observe(this) { makans ->
            // Update UI jika diperlukan, misal tampilkan daftar makanan di RecyclerView
            // makanAdapter.updateData(makans) -- jika Anda punya adapter
        }
    }

    private fun clearInputFields() {
        binding.inputNamaProduk.text?.clear()
        binding.inputHargaProduk.text?.clear()
        binding.inputDeskripsiProduk.text?.clear()
        binding.foto.setImageURI(null)
        binding.foto.visibility = View.GONE
        binding.spinnerKategori.setSelection(0)
    }

    // Fungsi untuk menampilkan gambar yang sudah disimpan dari penyimpanan internal
    fun displayImageFromStorage(imageName: String) {
        val file = File(filesDir, "app_images/$imageName")
        if (file.exists()) {
            binding.foto.setImageURI(Uri.fromFile(file))
            binding.foto.visibility = View.VISIBLE
        } else {
            binding.foto.setImageResource(R.drawable.placeholder_image) // Placeholder jika gambar tidak ditemukan
            binding.foto.visibility = View.VISIBLE
        }
    }

    // Function to convert URI to Bitmap
    private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
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
