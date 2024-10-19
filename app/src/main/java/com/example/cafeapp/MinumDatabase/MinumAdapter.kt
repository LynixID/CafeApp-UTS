//package com.example.cafeapp.MinumDatabase
//
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.cafeapp.Minum
//import com.example.cafeapp.R
//import java.io.File
//
//class MinumAdapter(
//    private val minumList: List<Minum>,
//    private val onItemClick: (Minum) -> Unit
//): RecyclerView.Adapter<MinumAdapter.MenuViewHolder>() {
//    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val namaMenu: TextView = view.findViewById(R.id.namaMenu)
//        val hargaMenu: TextView = view.findViewById(R.id.hargaMenu)
//        val fotoMenu: ImageView = view.findViewById(R.id.fotoMenu)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.test_item_menu, parent, false)
//        return MenuViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
//        val minum = minumList[position]
//
//        // Menampilkan nama dan harga minum
//        holder.namaMenu.text = minum.name
//        holder.hargaMenu.text = minum.harga.toString()
//
//        // Dapatkan path gambar dari direktori internal
//        val context = holder.itemView.context
//        val imgPath = File(context.filesDir, "app_images/${minum.namaFoto}")
//
//        if (imgPath.exists()) {
//            // Jika file gambar ada, set gambar ke ImageView
//            holder.fotoMenu.setImageURI(Uri.fromFile(imgPath))
//        } else {
//            // Jika gambar tidak ditemukan, tampilkan gambar default atau kosongkan
//            holder.fotoMenu.setImageResource(R.drawable.placeholder_image) // Ganti dengan placeholder yang sesuai
//        }
//
//        // Set listener untuk klik item
//        holder.itemView.setOnClickListener {
//            onItemClick(minum)
//        }
//    }
//
//    override fun getItemCount() = minumList.size
//}