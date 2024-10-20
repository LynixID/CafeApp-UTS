//package com.example.cafeapp.MakanDatabase
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import com.example.cafeapp.R
//import com.example.cafeapp.databinding.ActivityTestDatabase1Binding
//import java.io.File
//import java.io.IOException
//import com.example.cafeapp.R.drawable
//
//class TestDatabase1 : AppCompatActivity() {
//
//    private lateinit var binding: ActivityTestDatabase1Binding
//    private val makanViewModel: MakanViewModel by viewModels() // Inisialisasi ViewModel
//    private var imagePath: String? = null // Menyimpan path gambar yang dipilih
//
//    // ActivityResultLauncher
//    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityTestDatabase1Binding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Hapus inputan user saat mulai
//        binding.inputHarga.text?.clear()
//        binding.inputMenu.text?.clear()
//
//        // Tombol untuk pindah ke halaman berikutnya
//        binding.next.setOnClickListener {
//            val intent = Intent(this, TestDatabase2::class.java)
//            startActivity(intent)
//        }
//
//        // Tombol untuk menyimpan data menu
//        binding.btnSubmit.setOnClickListener {
//            val namaMakan = binding.inputMenu.text.toString().trim()
//            val hargaMakan = binding.inputHarga.text.toString().trim()
//            val kategori = binding.inputKategori.text.toString().trim()
//
//            if (namaMakan.isNotEmpty() && hargaMakan.isNotEmpty() && imagePath != null) {
//                // Membuat objek Makan
//                val makan = Makan(
//                    _id = 0,
//                    name = namaMakan,
//                    harga = hargaMakan.toInt(),
//                    kategori = kategori,
//                    desk = "",  // Deskripsi bisa ditambahkan jika diperlukan
//                    imagePath = imagePath!! // Nama file gambar, bukan path lengkap
//                )
//
//                // Simpan makan menggunakan ViewModel
//                makanViewModel.insertMakan(makan)
//
//                // Pindah ke TestDatabase2 setelah data disimpan
//                val intent = Intent(this, TestDatabase2::class.java)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // Konfigurasi untuk mengambil gambar dari galeri
//        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                result.data?.data?.let { uri ->
//                    binding.foto.setImageURI(uri) // Tampilkan gambar yang dipilih
//                    binding.foto.visibility = View.VISIBLE // Tampilkan ImageView
//
//                    // Ubah URI menjadi Bitmap
//                    val bitmap = getBitmapFromUri(this, uri)
//                    if (bitmap != null) {
//                        val imageName = "makan_${System.currentTimeMillis()}.png" // Nama file unik
//                        makanViewModel.saveImageToInternalStorage(bitmap, imageName)?.let { savedImageName ->
//                            imagePath = savedImageName // Simpan nama file gambar
//                        }
//                    }
//                }
//            }
//        }
//
//        // Listener untuk tombol upload gambar
//        binding.tambahFoto.setOnClickListener {
//            // Pilih gambar dari galeri
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            getImageLauncher.launch(intent)
//        }
//    }
//
//    // Fungsi untuk mengubah URI menjadi Bitmap
//    private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
//        return try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                val source = ImageDecoder.createSource(context.contentResolver, uri)
//                ImageDecoder.decodeBitmap(source)
//            } else {
//                context.contentResolver.openInputStream(uri)?.use {
//                    BitmapFactory.decodeStream(it)
//                }
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    // Fungsi untuk menampilkan gambar yang sudah disimpan dari penyimpanan internal
//    private fun displayImageFromStorage(imageName: String) {
//        val file = File(filesDir, "app_images/$imageName")
//        if (file.exists()) {
//            binding.foto.setImageURI(Uri.fromFile(file))
//            binding.foto.visibility = View.VISIBLE
//        } else {
//            binding.foto.setImageResource(R.drawable.placeholder_image) // Placeholder jika gambar tidak ditemukan
//            binding.foto.visibility = View.VISIBLE
//        }
//    }
//}
