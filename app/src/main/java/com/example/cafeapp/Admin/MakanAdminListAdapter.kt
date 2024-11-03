package com.example.cafeapp.Admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.R
import com.example.cafeapp.databinding.TestItemMenuBinding
import java.io.File

class MakanAdminListAdapter(
    private var makanList: List<Makan>, // Sama seperti sebelumnya
    private val listener: OnItemClickListener

) : RecyclerView.Adapter<MakanAdminListAdapter.MenuViewHolder>() {

    interface OnItemClickListener { // Interface tetap didefinisikan di sini
        fun onEditClick(item: Makan)
        fun onDeleteClick(item: Makan)
    }

    // Mengganti nama class ViewHolder sesuai dengan konvensi NumberListAdapter sebelumnya
//    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) { // Ganti MenuViewHolder ke MenuViewHolder
//        val itemdescription: TextView = view.findViewById(R.id.makan_deskripsi)
//        val btnHapus: ImageView = view.findViewById(R.id.makan_btn_hapus)
//        val namaMenu: TextView = view.findViewById(R.id.makan_nama)
//        val hargaMenu: TextView = view.findViewById(R.id.makan_harga)
//        val fotoMenu: ImageView = view.findViewById(R.id.makan_image)
//    }
    class MenuViewHolder(val binding: TestItemMenuBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TestItemMenuBinding.inflate(layoutInflater, parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) { // Parameter holder jadi tipe MenuViewHolder
        val makan = makanList[position]
        holder.binding.apply {
            makanNama.text = makan.name
            makanHarga.text = "Rp. ${makan.harga}"
            makanDeskripsi.text = makan.deskripsi

            // Dapatkan path gambar dari direktori internal
            val context = holder.itemView.context
            val imgPath = File(context.filesDir, "app_images/${makan.namaFoto}")

            if (imgPath.exists()) {
                makanImage.setImageURI(Uri.fromFile(imgPath))
            } else {
                makanImage.setImageResource(R.drawable.placeholder_image)
            }

            // Set listener untuk tombol hapus dan item klik
            makanBtnHapus.setOnClickListener { listener.onDeleteClick(makan) }
            root.setOnClickListener { listener.onEditClick(makan) }
        }
    }

    override fun getItemCount() = makanList.size
}
