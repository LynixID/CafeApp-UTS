package com.example.cafeapp.MakanDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ItemFoodBinding
import java.io.File

class MakanAdapter(
    private var makanList: List<Makan>,
    private val onItemClick: (Makan) -> Unit
) : RecyclerView.Adapter<MakanAdapter.MenuViewHolder>() {

    class MenuViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(makan: Makan, onItemClick: (Makan) -> Unit) {
            binding.foodName.text = makan.name
            binding.foodPrice.text = "Rp ${makan.harga}" // Menggunakan format mata uang

            // Pemuatan gambar dengan Glide
            val imgPath = File(binding.root.context.filesDir, makan.imagePath)
            Glide.with(binding.root.context)
                .load(imgPath)
                .placeholder(R.drawable.placeholder_image) // Placeholder saat gambar dimuat
                .into(binding.foodImage)

            // Menangani klik item
            binding.root.setOnClickListener { onItemClick(makan) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    fun updateData(newMakanList: List<Makan>) {
        makanList = newMakanList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = makanList[position]
        holder.bind(makan, onItemClick)
    }

    override fun getItemCount() = makanList.size
}
