package com.example.cafeapp.MakanDatabase

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import java.io.File

class MakanAdapter(
    private var makanList: List<Makan>,
    private val onItemClick: (Makan) -> Unit,
//    private val onEditClick: (Makan) -> Unit,
//    private val onDeleteClick: (Makan) -> Unit
) : RecyclerView.Adapter<MakanAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val namaMenu: TextView = view.findViewById(R.id.makan_nama)
//        val hargaMenu: TextView = view.findViewById(R.id.makan_harga)
//        val fotoMenu: ImageView = view.findViewById(R.id.makan_image)
        val itemdescription: TextView = view.findViewById(R.id.textViewFoodDescription)
//        val btnHapus: ImageView = view.findViewById(R.id.makan_btn_hapus)
        val namaMenu: TextView = view.findViewById(R.id.textViewFoodName)
        val hargaMenu: TextView = view.findViewById(R.id.textViewFoodPrice)
        val fotoMenu: ImageView = view.findViewById(R.id.imageViewFood)
//        val itemdescription: TextView = view.findViewById(R.id.makan_deskripsi)
//        val btnEdit: ImageButton = view.findViewById(R.id.makan_btn_edit)
//        val btnHapus: ImageButton = view.findViewById(R.id.makan_btn_hapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = makanList[position]

        // Menampilkan nama dan harga makan
        holder.namaMenu.text = makan.name
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

    override fun getItemCount() = makanList.size

    fun updateData(newMakanList: List<Makan>) {
        makanList = newMakanList
        notifyDataSetChanged() // Beritahu adapter bahwa data telah berubah
    }


}
