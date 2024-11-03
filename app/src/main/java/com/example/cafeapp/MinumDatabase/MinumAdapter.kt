package com.example.cafeapp.MinumDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ItemRecommendedBinding
import java.io.File

class MinumAdapter(
    private var minumList: List<Minum>,
    private val onItemClick: (Minum) -> Unit
) : RecyclerView.Adapter<MinumAdapter.MenuViewHolder>() {

    class MenuViewHolder(private val binding: ItemRecommendedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(minum: Minum, onItemClick: (Minum) -> Unit) {
            // Menampilkan nama dan harga minum
            binding.textViewFoodName.text = minum.name
            binding.textViewFoodPrice.text = "Rp. ${minum.harga}"
            binding.textViewFoodDescription.text = minum.deskripsi

            // Dapatkan path gambar dari direktori internal
            val context = binding.root.context
            val imgPath = File(context.filesDir, "app_images/${minum.namaFoto}")

            if (imgPath.exists()) {
                binding.imageViewFood.setImageURI(Uri.fromFile(imgPath))
            } else {
                binding.imageViewFood.setImageResource(R.drawable.placeholder_image)
            }

            // Set listener untuk klik seluruh item
            binding.root.setOnClickListener {
                onItemClick(minum) // Trigger saat seluruh item diklik
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val minum = minumList[position]
        holder.bind(minum, onItemClick)
    }

    override fun getItemCount() = minumList.size

    // Fungsi untuk memperbarui data
    fun updateData(newMinumList: List<Minum>) {
        minumList = newMinumList
        notifyDataSetChanged() // Beritahu adapter bahwa data telah berubah
    }
}
