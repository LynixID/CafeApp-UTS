package com.example.cafeapp.MenuDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import java.io.File

class MenuAdapter(
    private var menuList: List<Menu>,
    private val onItemClick: (Menu) -> Unit,
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemdescription: TextView = view.findViewById(R.id.textViewFoodDescription)
        val namaMenu: TextView = view.findViewById(R.id.textViewFoodName)
        val hargaMenu: TextView = view.findViewById(R.id.textViewFoodPrice)
        val fotoMenu: ImageView = view.findViewById(R.id.imageViewFood)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = menuList[position]

        // Menampilkan nama dan harga makan
        holder.namaMenu.text = makan.nama
        holder.hargaMenu.text = "Rp. ${makan.harga}" // Menambahkan simbol mata uang
        holder.itemdescription.text = makan.deskripsi

        // Dapatkan path gambar dari direktori internal
        val context = holder.itemView.context
        val imgPath = File(context.filesDir, "app_images/${makan.namaFoto}")

        if (imgPath.exists()) {
            holder.fotoMenu.setImageURI(Uri.fromFile(imgPath))
        } else {
            holder.fotoMenu.setImageResource(R.drawable.placeholder_image)
        }

        // Set listener untuk klik seluruh item
        holder.itemView.setOnClickListener {
            onItemClick(makan) // Trigger saat seluruh item diklik
        }
    }

    override fun getItemCount() = menuList.size

    fun updateData(newMenuList: List<Menu>) {
        menuList = newMenuList
        notifyDataSetChanged() // Beritahu adapter bahwa data telah berubah
    }


}
