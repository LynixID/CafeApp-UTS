package com.example.cafeapp.Admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MinumDatabase.Minum
import com.example.cafeapp.R
import java.io.File

class MinumAdminListAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<Minum, MinumAdminListAdapter.MenuViewHolder>(MinumDiffCallback()) {

    interface OnItemClickListener {
        fun onEditClick(item: Minum)
        fun onDeleteClick(item: Minum)
    }

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemDescription: TextView = view.findViewById(R.id.makan_deskripsi)
        val btnHapus: ImageView = view.findViewById(R.id.makan_btn_hapus)
        val namaMenu: TextView = view.findViewById(R.id.makan_nama)
        val hargaMenu: TextView = view.findViewById(R.id.makan_harga)
        val fotoMenu: ImageView = view.findViewById(R.id.makan_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_item_menu, parent, false)
        return MenuViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val minum = getItem(position)

        // Menampilkan nama dan harga minum
        holder.namaMenu.text = minum.name
        holder.hargaMenu.text = "Rp. ${minum.harga}"
        holder.itemDescription.text = minum.deskripsi

        // Dapatkan path gambar dari direktori internal
        val context = holder.itemView.context
        val imgPath = File(context.filesDir, "app_images/${minum.namaFoto}")

        if (imgPath.exists()) {
            holder.fotoMenu.setImageURI(Uri.fromFile(imgPath))
        } else {
            holder.fotoMenu.setImageResource(R.drawable.placeholder_image) // Ganti dengan gambar placeholder yang sesuai
        }

        // Set listener untuk klik item
        holder.btnHapus.setOnClickListener {
            listener.onDeleteClick(minum)
        }
        holder.itemView.setOnClickListener {
            listener.onEditClick(minum)
        }
    }

    fun updateData(newMinumList: List<Minum>) {
        submitList(newMinumList)
    }

    class MinumDiffCallback : DiffUtil.ItemCallback<Minum>() {
        override fun areItemsTheSame(oldItem: Minum, newItem: Minum): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Minum, newItem: Minum): Boolean {
            return oldItem == newItem
        }
    }
}
