//package com.example.cafeapp.TambahMenu
//
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.cafeapp.MakanDatabase.Makan
//import com.example.cafeapp.R
//import java.io.File
//
//class MenuBaruAdapter(
//    private val menuList: List<Makan>,
//    private val onEditClick: (Makan) -> Unit,
//    private val onDeleteClick: (Makan) -> Unit
//) : RecyclerView.Adapter<MenuBaruAdapter.MenuViewHolder>() {
//
//    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val itemImage: ImageView = view.findViewById(R.id.makan_image)
//        val itemName: TextView = view.findViewById(R.id.makan_nama)
//        val itemPrice: TextView = view.findViewById(R.id.makan_harga)
//        val itemdescription: TextView = view.findViewById(R.id.makan_deskripsi)
////        val btnEdit: ImageButton = view.findViewById(R.id.makan_btn_edit)
//        val btnHapus: ImageButton = view.findViewById(R.id.makan_btn_hapus)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.test_item_menu, parent, false)
//        return MenuViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
//        val menuItem = menuList[position]
//        holder.itemName.text = menuItem.name
//        holder.itemPrice.text = menuItem.harga.toString()
//        holder.itemdescription.text = "Enak Banget Uyy"
//        // Dapatkan path gambar dari direktori internal
//        val context = holder.itemView.context
//        val imgPath = File(context.filesDir, "app_images/${menuItem.namaFoto}")
//
//        if (imgPath.exists()) {
//            // Jika file gambar ada, set gambar ke ImageView
//            holder.itemImage.setImageURI(Uri.fromFile(imgPath))
//        } else {
//            // Jika gambar tidak ditemukan, tampilkan gambar default atau kosongkan
//            holder.itemImage.setImageResource(R.drawable.placeholder_image) // Ganti dengan placeholder yang sesuai
//        }
//
////        holder.btnEdit.setOnClickListener { onEditClick(menuItem) }
//        holder.btnHapus.setOnClickListener { onDeleteClick(menuItem) }
//    }
//
//    override fun getItemCount() = menuList.size
//}
//
