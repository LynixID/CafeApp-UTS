package com.example.cafeapp.MenuDatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafeapp.R
import com.example.cafeapp.databinding.MenuHeaderBinding
import com.example.cafeapp.databinding.MenuItemBinding

// Adapter untuk menampilkan menu dalam RecyclerView
class MenuAdapter(
    private var menuList: List<Menu>, // Daftar menu yang akan ditampilkan
    private val itemClickListener: (Menu) -> Unit // Listener untuk menangani klik pada item menu
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0 // Tipe view untuk header
        private const val TYPE_ITEM = 1 // Tipe view untuk item menu
    }

    // Mengembalikan daftar menu
    fun getMenuList(): List<Menu> = menuList

    // Menentukan tipe view berdasarkan posisi
    override fun getItemViewType(position: Int): Int {
        return if (menuList[position].nama == "Makanan" || menuList[position].nama == "Minuman") {
            TYPE_HEADER // Jika nama menu adalah kategori, gunakan header
        } else {
            TYPE_ITEM // Jika bukan, gunakan item biasa
        }
    }

    // Membuat ViewHolder berdasarkan tipe view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = MenuHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding) // ViewHolder untuk header
            }
            else -> {
                val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding) // ViewHolder untuk item
            }
        }
    }

    // Menghubungkan data dengan ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(menuList[position]) // Bind data ke header
            }
            is ItemViewHolder -> {
                val menu = menuList[position]
                if (menu._id != 0) { // Hanya bind listener jika bukan header
                    holder.bind(menu, itemClickListener) // Bind data ke item
                }
            }
        }
    }

    // Mendapatkan jumlah item dalam daftar menu
    override fun getItemCount(): Int = menuList.size

    // Memperbarui data menu dan memberi tahu adapter
    fun updateData(newMenuList: List<Menu>) {
        menuList = newMenuList
        notifyDataSetChanged() // Memberitahu RecyclerView bahwa data berubah
    }

    // ViewHolder untuk header
    class HeaderViewHolder(private val binding: MenuHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu) {
            binding.headerText.text = menu.nama // Mengatur teks header dengan nama kategori
        }
    }

    // ViewHolder untuk item menu
    class ItemViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun bind(menu: Menu, clickListener: (Menu) -> Unit) {
            binding.textViewFoodName.text = menu.nama // Nama menu
            binding.textViewFoodDescription.text = menu.deskripsi // Deskripsi menu
            binding.textViewFoodPrice.text = "Rp. ${menu.harga}" // Harga menu

            // Memuat gambar menggunakan Glide
            val imgPath = "${context.filesDir}/app_images/${menu.namaFoto}" // Path gambar
            Glide.with(context)
                .load(imgPath)
                .placeholder(R.drawable.placeholder_image) // Gambar sementara jika belum dimuat
                .error(R.drawable.placeholder_image) // Gambar default jika gagal dimuat
                .into(binding.imageViewFood)

            // Listener untuk klik pada item menu
            binding.root.setOnClickListener {
                clickListener(menu) // Menjalankan fungsi klik yang diteruskan
            }
        }
    }
}