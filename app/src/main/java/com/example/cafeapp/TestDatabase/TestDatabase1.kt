package com.example.cafeapp.TestDatabase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityTestDatabase1Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestDatabase1 : AppCompatActivity() {

    lateinit var binding: ActivityTestDatabase1Binding
    lateinit var database: ColorDatabase
    private var imagePath: String? = null // Menyimpan path gambar yang dipilih
    // Deklarasi ActivityResultLauncher
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTestDatabase1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = ColorDatabase.getInstance(this)

        binding.btnSubmit.setOnClickListener {
            val namaMenu = binding.inputMenu.text.toString().trim()
            val hargaMenu = binding.inputHarga.text.toString().trim()

            if (namaMenu.isNotEmpty()) {
                saveColorToDatabase(namaMenu, hargaMenu, imagePath!!)

                val intent = Intent(this, TestDatabase2::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Mauskkan Menu yang akan ditambah", Toast.LENGTH_SHORT).show()
            }
        }

        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.foto.setImageURI(uri) // Tampilkan gambar yang dipilih
                    binding.foto.visibility = View.VISIBLE // Tampilkan ImageView
                    imagePath = uri.toString() // Simpan path gambar
                }
            }
        }

        // Listener untuk tombol upload gambar
        binding.tambahFoto.setOnClickListener {
            // Pilih gambar dari galeri
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getImageLauncher.launch(intent) // Menggunakan ActivityResultLauncher
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                binding.foto.setImageURI(uri) // Tampilkan gambar yang dipilih
                binding.foto.visibility = View.VISIBLE // Tampilkan ImageView
                imagePath = uri.toString() // Simpan path gambar
            }
        }
    }

    private fun saveColorToDatabase(namaMenu: String, hargaMenu:String, imagePath: String) {
        // Menyimpan data ke database menggunakan Coroutine
        lifecycleScope.launch(Dispatchers.IO) {
            val color = Menu(0, namaMenu, hargaMenu, imagePath) // Menggunakan contoh HEX default
            database.colorDao().insert(color)

            // Memberi tahu pengguna bahwa data telah berhasil disimpan (di UI thread)
            runOnUiThread {
                Toast.makeText(this@TestDatabase1, "Menu Tersimpan di database", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        private const val PICK_IMAGE = 1
    }

}