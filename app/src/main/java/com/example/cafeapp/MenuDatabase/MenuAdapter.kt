package com.example.cafeapp.MenuDatabase

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.databinding.MenuItemBinding
import com.example.cafeapp.databinding.ItemHeaderBinding

class MenuAdapter(
    private var menuList: List<Menu>,
    private val itemClickListener: (Menu) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    fun getMenuList(): List<Menu> = menuList

    override fun getItemViewType(position: Int): Int {
        return if (menuList[position].nama == "Makanan" || menuList[position].nama == "Minuman") {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(menuList[position])
            }
            is ItemViewHolder -> {
                val menu = menuList[position]
                if (menu._id != 0) { // Hanya bind click listener jika bukan header
                    holder.bind(menu, itemClickListener)
                }
            }
        }
    }

    override fun getItemCount(): Int = menuList.size

    fun updateData(newMenuList: List<Menu>) {
        menuList = newMenuList
        notifyDataSetChanged()
    }

    class HeaderViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu) {
            binding.headerText.text = menu.nama
        }
    }

    class ItemViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun bind(menu: Menu, clickListener: (Menu) -> Unit) {
            binding.textViewFoodName.text = menu.nama
            binding.textViewFoodDescription.text = menu.deskripsi
            binding.textViewFoodPrice.text = "Rp. ${menu.harga}"

            val imgPath = Uri.parse("file://${context.filesDir}/app_images/${menu.namaFoto}")
            binding.imageViewFood.setImageURI(imgPath)

            binding.root.setOnClickListener {
                clickListener(menu)
            }
        }
    }
}