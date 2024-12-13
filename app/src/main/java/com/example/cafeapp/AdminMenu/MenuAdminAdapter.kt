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

class MenuAdminAdapter(
    private var menuList: List<Menu>, // Data list menu yang akan ditampilkan
    private val listener: OnItemClickListener // Listener untuk menangani event klik
) : RecyclerView.Adapter<MenuAdminAdapter.MenuViewHolder>() {

    // Interface untuk menangani klik edit dan delete pada item menu
    interface OnItemClickListener {
        fun onEditClick(item: Menu) // Dipanggil saat tombol edit diklik
        fun onDeleteClick(item: Menu) // Dipanggil saat tombol delete diklik
    }

    // ViewHolder untuk mengelola tampilan setiap item menu
    class MenuViewHolder(private val binding: ListItemMenuadminBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu, listener: OnItemClickListener) {
            // Menampilkan nama, harga, dan deskripsi menu
            binding.makanNama.text = menu.nama
            binding.makanHarga.text = "Rp. ${menu.harga}" // Format harga dengan simbol mata uang
            binding.makanDeskripsi.text = menu.deskripsi

            // Menampilkan gambar dari penyimpanan internal, atau gambar default jika tidak ditemukan
            val context = binding.root.context
            val imgPath = File(context.filesDir, "app_images/${menu.namaFoto}")

            // Menggunakan Glide untuk memuat gambar
            if (imgPath.exists()) {
                Glide.with(context)
                    .load(Uri.fromFile(imgPath)) // Memuat gambar dari file
                    .placeholder(R.drawable.placeholder_image) // Placeholder jika gambar tidak ada
                    .into(binding.makanImage)
            } else {
                Glide.with(context)
                    .load(R.drawable.placeholder_image) // Jika gambar tidak ditemukan, tampilkan placeholder
                    .into(binding.makanImage)
            }

            // Event klik untuk tombol hapus
            binding.makanBtnHapus.setOnClickListener {
                listener.onDeleteClick(menu)
            }

            // Event klik pada item menu untuk mengedit
            binding.root.setOnClickListener {
                listener.onEditClick(menu)
            }
        }
    }

    // Membuat ViewHolder baru untuk menampilkan item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ListItemMenuadminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    // Menghubungkan data menu ke ViewHolder
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = menuList[position] // Ambil data menu berdasarkan posisi
        holder.bind(makan, listener) // Pasang data dan listener
    }

    // Mengembalikan jumlah item dalam daftar menu
    override fun getItemCount() = menuList.size

    // Memperbarui data dalam adapter dan memberi tahu RecyclerView bahwa data telah berubah
    fun updateData(newMenuList: List<Menu>) {
        menuList = newMenuList // Perbarui daftar menu
        notifyDataSetChanged() // Refresh tampilan RecyclerView
    }
}