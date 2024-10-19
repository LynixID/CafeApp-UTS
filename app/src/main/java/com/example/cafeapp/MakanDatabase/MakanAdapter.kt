package com.example.cafeapp.MakanDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Makan
import com.example.cafeapp.R
import java.io.File

class MakanAdapter(
    private val makanList: List<Makan>,
    private val onItemClick: (Makan) -> Unit
) : RecyclerView.Adapter<MakanAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaMenu: TextView = view.findViewById(R.id.foodName)
        val hargaMenu: TextView = view.findViewById(R.id.foodPrice)
        val fotoMenu: ImageView = view.findViewById(R.id.food_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = makanList[position]

        // Menampilkan nama dan harga makan
        holder.namaMenu.text = makan.name
        holder.hargaMenu.text = "$${makan.harga}" // Menambahkan simbol mata uang

        // Dapatkan path gambar dari direktori internal
        val context = holder.itemView.context
        val imgPath = File(context.filesDir, "app_images/${makan.namaFoto}")

        if (imgPath.exists()) {
            // Jika file gambar ada, set gambar ke ImageView
            holder.fotoMenu.setImageURI(Uri.fromFile(imgPath))
        } else {
            // Jika gambar tidak ditemukan, tampilkan gambar default atau kosongkan
            holder.fotoMenu.setImageResource(R.drawable.placeholder_image) // Ganti dengan placeholder yang sesuai
        }

        // Set listener untuk klik item
        holder.itemView.setOnClickListener {
            onItemClick(makan)
        }
    }

    override fun getItemCount() = makanList.size
}
