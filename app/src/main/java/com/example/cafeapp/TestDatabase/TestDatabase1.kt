package com.example.cafeapp.TestDatabase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class TestDatabase1 : AppCompatActivity() {

    lateinit var binding: ActivityTestDatabase1Binding
    lateinit var database: MenuDatabase
    private var imagePath: String? = null // Menyimpan path gambar yang dipilih
    // Deklarasi ActivityResultLauncher
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityTestDatabase1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        //        Hapus inputan user
        binding.inputHarga.text?.clear()
        binding.inputMenu.text?.clear()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = MenuDatabase.getInstance(this)

        binding.next.setOnClickListener(){
            val intent = Intent(this, TestDatabase2::class.java)
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener {
            val namaMenu = binding.inputMenu.text.toString().trim()
            val hargaMenu = binding.inputHarga.text.toString().trim().toInt()

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

                    // Ubah URI menjadi Bitmap
                    val bitmap = getBitmapFromUri(this, uri)
                    if (bitmap != null) {
                        val imageName = "menu_${System.currentTimeMillis()}" // Gunakan nama file yang unik
                        val savedImageName = saveImageToInternalStorage(this, bitmap, imageName)
                        if (savedImageName != null) {
                            imagePath = savedImageName // Hanya simpan nama file, bukan path lengkap
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
            getImageLauncher.launch(intent) // Menggunakan ActivityResultLauncher
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                binding.foto.setImageURI(uri) // Tampilkan gambar yang dipilih
                binding.foto.visibility = View.VISIBLE // Tampilkan ImageView
            }
        }
    }

    private fun saveColorToDatabase(namaMenu: String, hargaMenu: Int, imagePath: String) {
        // Pastikan imagePath tidak null
        if (imagePath != null) {
            // Menyimpan data ke database menggunakan Coroutine
            lifecycleScope.launch(Dispatchers.IO) {
                val menu = Menu(0, namaMenu, hargaMenu, imagePath) // Menyimpan nama file gambar
                database.colorDao().insert(menu)

                // Memberi tahu pengguna bahwa data telah berhasil disimpan (di UI thread)
                runOnUiThread {
                    Toast.makeText(this@TestDatabase1, "Menu Tersimpan di database", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Jika tidak ada gambar yang dipilih
            Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }


    fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, imageName: String): String? {
        val directory = File(context.filesDir, "app_images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "$imageName.jpg")

        return try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            file.name // Hanya mengembalikan nama file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


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