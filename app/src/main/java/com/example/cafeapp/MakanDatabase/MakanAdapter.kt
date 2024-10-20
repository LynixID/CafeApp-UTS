package com.example.cafeapp.MakanDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import java.io.File

class MakanAdapter(
    private var makanList: List<Makan>,
    private val onItemClick: (Makan) -> Unit
) : RecyclerView.Adapter<MakanAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaMenu: TextView = view.findViewById(R.id.foodName)
        val hargaMenu: TextView = view.findViewById(R.id.foodPrice)
        val fotoMenu: ImageView = view.findViewById(R.id.food_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return MenuViewHolder(view)
    }

    fun updateData(newMakanList: List<Makan>) {
        makanList = newMakanList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = makanList[position]

        holder.namaMenu.text = makan.name
        holder.hargaMenu.text = "$${makan.harga}"

        val context = holder.itemView.context
        val imgPath = File(context.filesDir, makan.imagePath)
        if (imgPath.exists()) {
            holder.fotoMenu.setImageURI(Uri.fromFile(imgPath))
        } else {
            holder.fotoMenu.setImageResource(R.drawable.placeholder_image)
        }

        holder.itemView.setOnClickListener {
            onItemClick(makan)
        }
    }

    override fun getItemCount() = makanList.size
}
