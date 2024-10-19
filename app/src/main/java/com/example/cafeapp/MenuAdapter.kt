package com.example.cafeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.cafeapp.R
import androidx.recyclerview.widget.RecyclerView


class MenuAdapter(
    private val menuList: List<Menu>,
    private val onItemClick: (Menu) -> Unit) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageMenu: ImageView = view.findViewById(R.id.imageProduct)
        val textMenuName: TextView = view.findViewById(R.id.textProductName)
        val textMenuPrice: TextView = view.findViewById(R.id.textProductPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_detail, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menuList[position]
        holder.imageMenu.setImageResource(menu.imageResId)
        holder.textMenuName.text = menu.name
        holder.textMenuPrice.text = menu.price

        holder.itemView.setOnClickListener {
            onItemClick(menu)
        }
    }

    override fun getItemCount() = menuList.size
}
