package com.example.cafeapp.MakanDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ItemRecommendedBinding
import java.io.File

class MakanListAdapter(
    private val onItemClick: (Makan) -> Unit
) : ListAdapter<Makan, MakanListAdapter.MenuViewHolder>(MakanDiffCallback()) {

    class MenuViewHolder(private val binding: ItemRecommendedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(makan: Makan, onItemClick: (Makan) -> Unit) {
            binding.textViewFoodName.text = makan.name
            binding.textViewFoodPrice.text = "Rp. ${makan.harga}"
            binding.textViewFoodDescription.text = makan.deskripsi

            // Dapatkan path gambar dari direktori internal
            val context = binding.root.context
            val imgPath = File(context.filesDir, "app_images/${makan.namaFoto}")

            if (imgPath.exists()) {
                binding.imageViewFood.setImageURI(Uri.fromFile(imgPath))
            } else {
                binding.imageViewFood.setImageResource(R.drawable.placeholder_image)
            }

            // Set listener untuk klik seluruh item
            binding.root.setOnClickListener {
                onItemClick(makan)
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
    class MakanDiffCallback : DiffUtil.ItemCallback<Makan>() {
        override fun areItemsTheSame(oldItem: Makan, newItem: Makan): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Makan, newItem: Makan): Boolean {
            return oldItem == newItem
        }
    }

    // Fungsi untuk memperbarui data
    fun updateData(newMakanList: List<Makan>) {
        submitList(newMakanList) // Gunakan submitList untuk memperbarui data dengan efisien
    }
}
