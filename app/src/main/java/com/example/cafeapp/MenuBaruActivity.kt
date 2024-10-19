package com.example.cafeapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuBaruActivity : AppCompatActivity() {

    private val viewModel: MenuBaruViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuAdapter: MenuBaruAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_all_menu)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observer untuk mendapatkan data dari ViewModel
        viewModel.menuList.observe(this) { menuItems ->
            menuAdapter = MenuBaruAdapter(menuItems!!, { item ->
                // Logika untuk mengedit item
                // Tambahkan kode untuk mengedit item di sini
            }, { item ->
                // Logika untuk menghapus item
                viewModel.deleteItem(item)
            })
            recyclerView.adapter = menuAdapter
        }

        // Tombol back untuk kembali ke halaman sebelumnya
        val buttonBack : ImageButton = findViewById(R.id.btnBack)
        buttonBack.setOnClickListener {
            finish()
        }

    }

}
