package com.example.cafeapp.MenuDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ItemRecommendedBinding
import java.io.File

class MenuAdapter(
    private var menuList: List<Menu>,
    private val onItemClick: (Menu) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    // ViewHolder using ViewBinding
    class MenuViewHolder(private val binding: ItemRecommendedBinding) : RecyclerView.ViewHolder(binding.root) {
        // Direct access to views via binding
        fun bind(menu: Menu, onItemClick: (Menu) -> Unit) {
            binding.textViewFoodName.text = menu.nama
            binding.textViewFoodPrice.text = "Rp. ${menu.harga}"
            binding.textViewFoodDescription.text = menu.deskripsi

            // Get the image path from internal storage
            val context = binding.root.context
            val imgPath = File(context.filesDir, "app_images/${menu.namaFoto}")

            if (imgPath.exists()) {
                binding.imageViewFood.setImageURI(Uri.fromFile(imgPath))
            } else {
                binding.imageViewFood.setImageResource(R.drawable.placeholder_image)
            }

            // Set listener for item click
            binding.root.setOnClickListener {
                onItemClick(menu) // Trigger click action
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = menuList[position]
        holder.bind(makan, onItemClick) // Bind data to the ViewHolder
    }

    override fun getItemCount() = menuList.size

    fun updateData(newMenuList: List<Menu>) {
        menuList = newMenuList
        notifyDataSetChanged() // Notify adapter that data has changed
    }
}
