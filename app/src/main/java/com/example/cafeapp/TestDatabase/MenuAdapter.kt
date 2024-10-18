package com.example.cafeapp.TestDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import java.io.File

class MenuAdapter(
    private val colorList: List<Menu>,
    private val onItemClick: (Menu) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorName: TextView = view.findViewById(R.id.namaMenu)
        val hexMenu: TextView = view.findViewById(R.id.hargaMenu)
        val fotoMenu: ImageView = view.findViewById(R.id.fotoMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = colorList[position]

        // Menampilkan nama dan harga menu
        holder.colorName.text = menu.name
        holder.hexMenu.text = menu.harga.toString()

        // Dapatkan path gambar dari direktori internal
        val context = holder.itemView.context
        val imgPath = File(context.filesDir, "app_images/${menu.imagePath}")

        if (imgPath.exists()) {
            // Jika file gambar ada, set gambar ke ImageView
            holder.fotoMenu.setImageURI(Uri.fromFile(imgPath))
        } else {
            // Jika gambar tidak ditemukan, tampilkan gambar default atau kosongkan
            holder.fotoMenu.setImageResource(R.drawable.placeholder_image) // Ganti dengan placeholder yang sesuai
        }

        // Set listener untuk klik item
        holder.itemView.setOnClickListener {
            onItemClick(menu)
        }
    }


    override fun getItemCount() = colorList.size
}