package com.example.cafeapp.Admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.R
import com.example.cafeapp.databinding.TestItemMenuBinding
import java.io.File

class MenuAdminAdapter(
    private var menuList: List<Menu>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MenuAdminAdapter.MenuViewHolder>() {

    interface OnItemClickListener {
        fun onEditClick(item: Menu)
        fun onDeleteClick(item: Menu)
    }

    class MenuViewHolder(private val binding: TestItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu, listener: OnItemClickListener) {
            // Menampilkan nama dan harga makan
            binding.makanNama.text = menu.nama
            binding.makanHarga.text = "Rp. ${menu.harga}" // Menambahkan simbol mata uang
            binding.makanDeskripsi.text = menu.deskripsi

            // Dapatkan path gambar dari direktori internal
            val context = binding.root.context
            val imgPath = File(context.filesDir, "app_images/${menu.namaFoto}")

            if (imgPath.exists()) {
                // Jika file gambar ada, set gambar ke ImageView
                binding.makanImage.setImageURI(Uri.fromFile(imgPath))
            } else {
                // Jika gambar tidak ditemukan, tampilkan gambar default
                binding.makanImage.setImageResource(R.drawable.placeholder_image)
            }

            // Set listener untuk klik item
            binding.makanBtnHapus.setOnClickListener {
                listener.onDeleteClick(menu)
            }
            binding.root.setOnClickListener {
                listener.onEditClick(menu) // Panggil callback saat item diklik
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = TestItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = menuList[position]
        holder.bind(makan, listener)
    }

    override fun getItemCount() = menuList.size

    fun updateData(newMenuList: List<Menu>) {
        menuList = newMenuList
        notifyDataSetChanged() // Beritahu adapter bahwa data telah berubah
    }
}
