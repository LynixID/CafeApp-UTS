package com.example.cafeapp.MinumDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ItemRecommendedBinding // Ganti dengan binding yang sesuai
import java.io.File

class MinumListAdapter(
    private val onItemClick: (Minum) -> Unit
) : ListAdapter<Minum, MinumListAdapter.MinumViewHolder>(MinumDiffCallback()) {

    class MinumViewHolder private constructor(val binding: ItemRecommendedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MinumViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRecommendedBinding.inflate(layoutInflater, parent, false)
                return MinumViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinumViewHolder {
        return MinumViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MinumViewHolder, position: Int) {
        val minum = getItem(position)

        // Menampilkan nama dan harga minum
        holder.binding.textViewFoodName.text = minum.name
        holder.binding.textViewFoodPrice.text = "Rp. ${minum.harga}"
        holder.binding.textViewFoodDescription.text = minum.deskripsi

        // Dapatkan path gambar dari direktori internal
        val context = holder.itemView.context
        val imgPath = File(context.filesDir, "app_images/${minum.namaFoto}")

        if (imgPath.exists()) {
            holder.binding.imageViewFood.setImageURI(Uri.fromFile(imgPath))
        } else {
            holder.binding.imageViewFood.setImageResource(R.drawable.placeholder_image)
        }

        // Set listener untuk klik seluruh item
        holder.itemView.setOnClickListener {
            onItemClick(minum) // Trigger saat seluruh item diklik
        }
    }

    // DiffCallback untuk perbandingan item
    class MinumDiffCallback : DiffUtil.ItemCallback<Minum>() {
        override fun areItemsTheSame(oldItem: Minum, newItem: Minum): Boolean {
            return oldItem._id == newItem._id // Ubah sesuai dengan identifier unik di Minum
        }

        override fun areContentsTheSame(oldItem: Minum, newItem: Minum): Boolean {
            return oldItem == newItem
        }
    }
}
