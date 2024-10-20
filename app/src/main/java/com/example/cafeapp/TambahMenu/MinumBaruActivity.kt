package com.example.cafeapp.TambahMenu

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R

class MinumBaruActivity : AppCompatActivity() {

    private val viewModel: MinumBaruViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var minumAdapter: MinumBaruAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_all_minuman)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observer untuk mendapatkan data dari ViewModel
        viewModel.menuList.observe(this) { menuItems ->
            minumAdapter = MinumBaruAdapter(menuItems!!, { item ->
                // Logika untuk mengedit item
                // Tambahkan kode untuk mengedit item di sini
            }, { item ->
                // Logika untuk menghapus item
                viewModel.deleteItem(item)
            })
            recyclerView.adapter = minumAdapter
        }

        // Tombol back untuk kembali ke halaman sebelumnya
        val buttonBack : ImageButton = findViewById(R.id.btnBack)
        buttonBack.setOnClickListener {
            finish()
        }

    }
}
