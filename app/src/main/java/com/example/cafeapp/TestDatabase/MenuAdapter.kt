package com.example.cafeapp.TestDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R

class MenuAdapter(
    private val colorList: List<Menu>,
    private val onItemClick: (Menu) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorName: TextView = view.findViewById(R.id.namaMenu)
        val hexMenu: TextView = view.findViewById(R.id.hargaMenu)
        val fotoMenu: ImageView = view.findViewById(R.id.fotoMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val color = colorList[position]
        holder.colorName.text = color.name
        holder.hexMenu.text = color.hex
        holder.fotoMenu.setImageURI(Uri.parse(color.imagePath))

        holder.itemView.setOnClickListener {
            onItemClick(color)
        }
    }

    override fun getItemCount() = colorList.size
}