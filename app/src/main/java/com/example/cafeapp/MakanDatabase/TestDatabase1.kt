package com.example.cafeapp.MakanDatabase

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
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.Makan
import com.example.cafeapp.Minum
import com.example.cafeapp.MinumDatabase.MinumViewModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityTestDatabase1Binding
import java.io.IOException

class TestDatabase1 : AppCompatActivity() {

    private lateinit var binding: ActivityTestDatabase1Binding
    private val makanViewModel: MakanViewModel by viewModels()
    private val minumViewModel: MinumViewModel by viewModels()// Inisialisasi ViewModel
    private var imagePath: String? = null // Menyimpan path gambar yang dipilih

    // ActivityResultLauncher
    private lateinit var tampilkanFotoSementara: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestDatabase1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hapus inputan user saat mulai
        binding.inputHarga.text?.clear()
        binding.inputMenu.text?.clear()

        // Membuat adapter dari array yang ada di strings.xml
        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.opsi_menu, // Array dari strings.xml
            android.R.layout.simple_spinner_item // Tampilan dasar dropdown
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter // Mengatur adapter untuk Spinner
        }

        binding.next.setOnClickListener {
            val intent = Intent(this, TestDatabase2::class.java)
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener {
            val namaMenu = binding.inputMenu.text.toString().trim()
            val hargaMakan = binding.inputHarga.text.toString().trim()
            val kategori = spinner.selectedItem.toString()

            if (namaMenu.isNotEmpty() && hargaMakan.isNotEmpty() && imagePath != null) {


                if(kategori == "Makanan"){
                    // Membuat objek Makan
                    val menu = Makan(
                        _id = 0,
                        name = namaMenu,
                        harga = hargaMakan.toInt(),
                        namaFoto = imagePath!! // Nama file gambar, bukan path lengkap
                    )
                    // Simpan makan menggunakan ViewModel
                    makanViewModel.insertMakan(menu)
                }else if (kategori == "Minuman"){
                    // Membuat objek Minum
                    val menu = Minum(
                        _id = 0,
                        name = namaMenu,
                        harga = hargaMakan.toInt(),
                        namaFoto = imagePath!! // Nama file gambar, bukan path lengkap
                    )
                    // Simpan makan menggunakan ViewModel
                    minumViewModel.insertMinum(menu)

                }else{

                }





                // Pindah ke TestDatabase2 setelah data disimpan
                val intent = Intent(this, TestDatabase2::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
            }
        }

        tampilkanFotoSementara = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.foto.setImageURI(uri) // Tampilkan gambar yang dipilih
                    binding.foto.visibility = View.VISIBLE // Tampilkan ImageView

                    // Ubah URI menjadi Bitmap
                    val bitmap = getBitmapFromUri(this, uri)
                    if (bitmap != null) {
                        val imageName = "makan_${System.currentTimeMillis()}" // Nama file unik
                        makanViewModel.saveImageToInternalStorage(bitmap, imageName)?.let { savedImageName ->
                            imagePath = savedImageName // Simpan nama file gambar
                        }
                    }
                }
            }
        }

        // Listener untuk tombol upload gambar
        binding.tambahFoto.setOnClickListener {
            // Pilih gambar dari galeri
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            tampilkanFotoSementara.launch(intent)
        }


    }

    // Fungsi untuk mengubah URI menjadi Bitmap
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

    companion object {
        private const val PICK_IMAGE = 1
    }
}