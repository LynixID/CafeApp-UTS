package com.example.cafeapp.TestDatabase

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityTestDatabase2Binding
import kotlinx.coroutines.launch

class TestDatabase2 : AppCompatActivity() {

    private lateinit var binding: ActivityTestDatabase2Binding
    private lateinit var database: ColorDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi view binding
        binding = ActivityTestDatabase2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi database
        database = ColorDatabase.getInstance(this)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Mengambil data dari database dan menghubungkannya dengan RecyclerView
        database.colorDao().getAll().observe(this) { menus ->
            // Pass the delete functionality to the adapter
            binding.recyclerView.adapter = MenuAdapter(menus) { menu ->
                DialogKonfirmasi(menu)
            }
        }
    }

    private fun deleteMenu(menu: Menu) {
        // Delete the selected menu item
        lifecycleScope.launch {
            database.colorDao().deleteById(menu._id)
        }
    }

    private fun DialogKonfirmasi(menu: Menu) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Are you sure you want to delete ${menu.name}?")

        builder.setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
            deleteMenu(menu) // Call deleteMenu if user confirms
        }

        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss() // Close dialog on cancel
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}