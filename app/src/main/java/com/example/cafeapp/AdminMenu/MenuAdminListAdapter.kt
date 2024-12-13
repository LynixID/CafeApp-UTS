package com.example.cafeapp.AdminMenu

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ListItemMenuadminBinding
import java.io.File

class MenuAdminListAdapter(
    private var menuList: List<Menu>, // Daftar menu
    private val listener: OnItemClickListener // Listener untuk event klik
) : RecyclerView.Adapter<MenuAdminListAdapter.MenuViewHolder>() {

    // Interface untuk menangani event klik
    interface OnItemClickListener {
        fun onEditClick(item: Menu)
        fun onDeleteClick(item: Menu)
    }

    // ViewHolder untuk item menu
    class MenuViewHolder(val binding: ListItemMenuadminBinding) : RecyclerView.ViewHolder(binding.root)

    // Membuat ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemMenuadminBinding.inflate(layoutInflater, parent, false)
        return MenuViewHolder(binding)
    }

    // Menghubungkan data dengan ViewHolder
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = menuList[position]
        holder.binding.apply {
            makanNama.text = makan.nama
            makanHarga.text = "Rp. ${makan.harga}"
            makanDeskripsi.text = makan.deskripsi

            val context = holder.itemView.context
            val imgPath = File(context.filesDir, "app_images/${makan.namaFoto}")
            if (imgPath.exists()) {
                // Menggunakan Glide untuk memuat gambar
                Glide.with(context)
                    .load(Uri.fromFile(imgPath))  // Memuat gambar dari file
                    .placeholder(R.drawable.placeholder_image)  // Placeholder jika gambar tidak ada
                    .into(makanImage)
            } else {
                // Jika gambar tidak ada, gunakan placeholder
                Glide.with(context)
                    .load(R.drawable.placeholder_image)
                    .into(makanImage)
            }

            makanBtnHapus.setOnClickListener { listener.onDeleteClick(makan) }
            root.setOnClickListener { listener.onEditClick(makan) }
        }
    }

    // Mendapatkan jumlah item
    override fun getItemCount() = menuList.size
}