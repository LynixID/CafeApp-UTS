package com.example.cafeapp.MinumDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import java.io.File

class MinumAdapter(
    private var minumList: List<Minum>, // Ubah menjadi var agar bisa di-update
    private val onItemClick: (Minum) -> Unit
) : RecyclerView.Adapter<MinumAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaMenu: TextView = view.findViewById(R.id.textViewFoodName)
        val hargaMenu: TextView = view.findViewById(R.id.textViewFoodPrice)
        val fotoMenu: ImageView = view.findViewById(R.id.imageViewFood)
        val itemdescription: TextView = view.findViewById(R.id.textViewFoodDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val minum = minumList[position]

        // Menampilkan nama dan harga minum
        holder.namaMenu.text = minum.name
        holder.hargaMenu.text = "Rp. ${minum.harga}"
        holder.itemdescription.text = minum.deskripsi

        // Dapatkan path gambar dari direktori internal
        val context = holder.itemView.context
        val imgPath = File(context.filesDir, "app_images/${minum.namaFoto}")

        if (imgPath.exists()) {
            holder.fotoMenu.setImageURI(Uri.fromFile(imgPath))
        } else {
            holder.fotoMenu.setImageResource(R.drawable.placeholder_image)
        }

        // Set listener untuk klik seluruh item
        holder.itemView.setOnClickListener {
            onItemClick(minum) // Trigger saat seluruh item diklik
        }
    }

    override fun getItemCount() = minumList.size

    // Fungsi untuk memperbarui data
    fun updateData(newMinumList: List<Minum>) {
        minumList = newMinumList
        notifyDataSetChanged() // Beritahu adapter bahwa data telah berubah
    }
}
