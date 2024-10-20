package com.example.cafeapp.MakanDatabase

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val itemdescription: TextView = view.findViewById(R.id.makan_deskripsi)
        val btnHapus: ImageView = view.findViewById(R.id.makan_btn_hapus)
        val namaMenu: TextView = view.findViewById(R.id.makan_nama)
        val hargaMenu: TextView = view.findViewById(R.id.makan_harga)
        val fotoMenu: ImageView = view.findViewById(R.id.makan_image)
//        val itemdescription: TextView = view.findViewById(R.id.makan_deskripsi)
//        val btnEdit: ImageButton = view.findViewById(R.id.makan_btn_edit)
//        val btnHapus: ImageButton = view.findViewById(R.id.makan_btn_hapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_item_menu, parent, false)
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
            // Jika file gambar ada, set gambar ke ImageView
            holder.fotoMenu.setImageURI(Uri.fromFile(imgPath))
        } else {
            // Jika gambar tidak ditemukan, tampilkan gambar default atau kosongkan
            holder.fotoMenu.setImageResource(R.drawable.placeholder_image) // Ganti dengan placeholder yang sesuai
        }

//         Set listener untuk klik item
        holder.btnHapus.setOnClickListener {
            onItemClick(makan)
        }
//        holder.btnEdit.setOnClickListener { onEditClick(makan) }
//        holder.btnHapus.setOnClickListener { onDeleteClick(makan) }
    }

    override fun getItemCount() = makanList.size

    fun updateData(newMakanList: List<Makan>) {
        makanList = newMakanList
        notifyDataSetChanged() // Beritahu adapter bahwa data telah berubah
    }
}
