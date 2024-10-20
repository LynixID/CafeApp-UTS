package com.example.cafeapp.TambahMenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.item_menuBaru

class MinumBaruAdapter(
    private val menuList: List<item_menuBaru>,
    private val onEditClick: (item_menuBaru) -> Unit,
    private val onDeleteClick: (item_menuBaru) -> Unit
) : RecyclerView.Adapter<MinumBaruAdapter.MinumViewHolder>() {

    class MinumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.makan_image)
        val itemName: TextView = view.findViewById(R.id.makan_nama)
        val itemPrice: TextView = view.findViewById(R.id.makan_harga)
        val itemdescription: TextView = view.findViewById(R.id.makan_deskripsi)
        val btnEdit: ImageButton = view.findViewById(R.id.makan_btn_edit)
        val btnHapus: ImageButton = view.findViewById(R.id.makan_btn_hapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_item_minuman, parent, false)
        return MinumViewHolder(view)
    }

    override fun onBindViewHolder(holder: MinumViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.itemName.text = menuItem.name
        holder.itemPrice.text = menuItem.price
        holder.itemImage.setImageResource(menuItem.imageResId)
        holder.itemdescription.text = menuItem.description
        holder.btnEdit.setOnClickListener { onEditClick(menuItem) }
        holder.btnHapus.setOnClickListener { onDeleteClick(menuItem) }
    }

    override fun getItemCount() = menuList.size
}

