package com.example.cafeapp.MenuDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ItemRecommendedBinding
import java.io.File

class MenuListAdapter(
    private val onItemClick: (Menu) -> Unit
) : ListAdapter<Menu, MenuListAdapter.MenuViewHolder>(MakanDiffCallback()) {

    class MenuViewHolder(private val binding: ItemRecommendedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu, onItemClick: (Menu) -> Unit) {
            binding.textViewFoodName.text = menu.nama
            binding.textViewFoodPrice.text = "Rp. ${menu.harga}"
            binding.textViewFoodDescription.text = menu.deskripsi

            // Dapatkan path gambar dari direktori internal
            val context = binding.root.context
            val imgPath = File(context.filesDir, "app_images/${menu.namaFoto}")

            if (imgPath.exists()) {
                binding.imageViewFood.setImageURI(Uri.fromFile(imgPath))
            } else {
                binding.imageViewFood.setImageResource(R.drawable.placeholder_image)
            }

            // Set listener untuk klik seluruh item
            binding.root.setOnClickListener {
                onItemClick(menu)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = getItem(position)
        holder.bind(makan, onItemClick)
    }

    // Kelas DiffUtil untuk membandingkan item
    class MakanDiffCallback : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem == newItem
        }
    }

    // Fungsi untuk memperbarui data
    fun updateData(newMenuList: List<Menu>) {
        submitList(newMenuList) // Gunakan submitList untuk memperbarui data dengan efisien
    }
}
