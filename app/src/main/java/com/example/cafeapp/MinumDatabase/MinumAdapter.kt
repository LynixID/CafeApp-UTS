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
    private val minumList: List<Minum>,
    private val onItemClick: (Minum) -> Unit,
//    private val onEditClick: (Minum) -> Unit,
//    private val onDeleteClick: (Minum) -> Unit
): RecyclerView.Adapter<MinumAdapter.MenuViewHolder>() {
    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaMenu: TextView = view.findViewById(R.id.makan_nama)
        val hargaMenu: TextView = view.findViewById(R.id.makan_harga)
        val fotoMenu: ImageView = view.findViewById(R.id.makan_image)
        val itemdescription: TextView = view.findViewById(R.id.makan_deskripsi)
//        val btnEdit: ImageButton = view.findViewById(R.id.minum_btn_edit)
//        val btnHapus: ImageButton = view.findViewById(R.id.minum_btn_hapus)
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
        holder.hargaMenu.text = "Rp. ${minum.harga}"
        holder.itemdescription.text = minum.deskripsi
//        holder.itemdescription.text = "Enak Banget Uyy"

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

        // Set listener untuk klik item
        holder.itemView.setOnClickListener {
            onItemClick(minum)
        }
//        holder.btnEdit.setOnClickListener { onEditClick(minum) }
//        holder.btnHapus.setOnClickListener { onDeleteClick(minum) }
    }

    override fun getItemCount() = minumList.size
}