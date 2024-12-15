package com.example.cafeapp.MenuDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafeapp.R
import com.example.cafeapp.databinding.MenuItemBinding
import java.io.File

// Adapter untuk menampilkan daftar menu menggunakan ListAdapter
class MenuListAdapter(
    private val onItemClick: (Menu) -> Unit // Listener untuk menangani klik pada item
) : ListAdapter<Menu, MenuListAdapter.MenuViewHolder>(MakanDiffCallback()) {

    // ViewHolder untuk item menu
    class MenuViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu, onItemClick: (Menu) -> Unit) {
            // Bind data menu ke elemen UI
            binding.textViewFoodName.text = menu.nama // Nama menu
            binding.textViewFoodPrice.text = "Rp. ${menu.harga}" // Harga menu
            binding.textViewFoodDescription.text = menu.deskripsi // Deskripsi menu

            // Memuat gambar menu dari direktori internal menggunakan Glide
            val context = binding.root.context
            val imgPath = File(context.filesDir, "app_images/${menu.namaFoto}")

            if (imgPath.exists()) {
                // Menggunakan Glide untuk memuat gambar
                Glide.with(context)
                    .load(Uri.fromFile(imgPath)) // Memuat gambar dari file
                    .placeholder(R.drawable.placeholder_image) // Placeholder jika gambar tidak ditemukan
                    .into(binding.imageViewFood) // Menampilkan gambar pada ImageView
            } else {
                // Jika gambar tidak ada, tampilkan placeholder menggunakan Glide
                Glide.with(context)
                    .load(R.drawable.placeholder_image) // Menampilkan gambar placeholder
                    .into(binding.imageViewFood)
            }

            // Listener untuk klik pada item menu
            binding.root.setOnClickListener {
                onItemClick(menu) // Memanggil callback klik
            }
        }
    }

    // Membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    // Menghubungkan data menu dengan ViewHolder
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = getItem(position) // Mengambil item dari daftar
        holder.bind(makan, onItemClick) // Bind data ke ViewHolder
    }

    // DiffUtil untuk membandingkan item dalam daftar
    class MakanDiffCallback : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem._id == newItem._id // Periksa apakah item sama berdasarkan ID
        }

        override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem == newItem // Periksa apakah konten item sama
        }
    }

    // Fungsi untuk memperbarui data menu dengan submitList
    fun updateData(newMenuList: List<Menu>) {
        submitList(newMenuList) // Memperbarui daftar dengan efisiensi tinggi
    }
}