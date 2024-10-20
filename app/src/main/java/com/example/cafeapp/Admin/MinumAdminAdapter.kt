package com.example.cafeapp.Admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MinumDatabase.Minum

import com.example.cafeapp.R
import java.io.File

class MinumAdminAdapter(
    private var minumList: List<Minum>,
    private val listener: OnItemClickListener

) : RecyclerView.Adapter<MinumAdminAdapter.MenuViewHolder>() {

    interface OnItemClickListener { // Interface didefinisikan di sini
        fun onEditClick(item: Minum)
        fun onDeleteClick(item: Minum)
    }

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemdescription: TextView = view.findViewById(R.id.makan_deskripsi)
        val btnHapus: ImageView = view.findViewById(R.id.makan_btn_hapus)
        val namaMenu: TextView = view.findViewById(R.id.makan_nama)
        val hargaMenu: TextView = view.findViewById(R.id.makan_harga)
        val fotoMenu: ImageView = view.findViewById(R.id.makan_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val minum = minumList[position]

        // Menampilkan nama dan harga minum
        holder.namaMenu.text = minum.name
        holder.hargaMenu.text = "Rp. ${minum.harga}" // Menambahkan simbol mata uang
        holder.itemdescription.text = minum.deskripsi

        // Dapatkan path gambar dari direktori internal
        val context = holder.itemView.context
        val imgPath = File(context.filesDir, "app_images/${minum.namaFoto}")

        if (imgPath.exists()) {
            // Jika file gambar ada, set gambar ke ImageView
            holder.fotoMenu.setImageURI(Uri.fromFile(imgPath))
        } else {
            // Jika gambar tidak ditemukan, tampilkan gambar default atau kosongkan
            holder.fotoMenu.setImageResource(R.drawable.placeholder_image) // Ganti dengan placeholder yang sesuai
        }

//         Set listener untuk klik item
        holder.btnHapus.setOnClickListener {
            listener.onDeleteClick(minum)
        }
        holder.itemView.setOnClickListener {
            listener.onEditClick(minum) // Panggil callback saat item diklik
        }

    }

    override fun getItemCount() = minumList.size

    fun updateData(newMinumList: List<Minum>) {
        minumList = newMinumList
        notifyDataSetChanged() // Beritahu adapter bahwa data telah berubah
    }
}

