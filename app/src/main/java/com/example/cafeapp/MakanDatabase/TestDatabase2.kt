//package com.example.cafeapp.MakanDatabase
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.viewModels
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.cafeapp.databinding.ActivityTestDatabase2Binding
//
//class TestDatabase2 : AppCompatActivity() {
//    private lateinit var binding: ActivityTestDatabase2Binding
//    private val menuViewModel: MakanViewModel by viewModels() // Inisialisasi ViewModel
//    private lateinit var makanAdapter: MakanAdapter // Inisialisasi adapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Inisialisasi binding
//        binding = ActivityTestDatabase2Binding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Setup RecyclerView
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // Inisialisasi Adapter dengan list kosong dan set adapter ke RecyclerView
//        makanAdapter = MakanAdapter(emptyList()) { menu ->
//            DialogKonfirmasi(menu) // Menampilkan dialog konfirmasi ketika item diklik
//        }
//        binding.recyclerView.adapter = makanAdapter
//
//        // Mengamati data dari ViewModel
//        menuViewModel.allMakans.observe(this) { menus ->
//            // Update adapter dengan daftar makanan baru
//            makanAdapter.updateData(menus)
//        }
//
//        // Tombol back untuk navigasi ke TestDatabase1
//        binding.back.setOnClickListener {
//            val intent = Intent(this, TestDatabase1::class.java)
//            startActivity(intent)
//        }
//    }
//
//    // Dialog konfirmasi untuk menghapus item menu
//    private fun DialogKonfirmasi(menu: Makan) {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Delete Confirmation")
//        builder.setMessage("Are you sure you want to delete ${menu.name}?")
//
//        builder.setPositiveButton("Yes") { _, _ ->
//            menuViewModel.deleteMakanById(menu._id) // Menghapus menu menggunakan ViewModel
//        }
//
//        builder.setNegativeButton("No") { dialog, _ ->
//            dialog.dismiss() // Tutup dialog jika pengguna membatalkan
//        }
//
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//    }
//}
