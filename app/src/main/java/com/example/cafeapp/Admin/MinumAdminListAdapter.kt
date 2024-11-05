package com.example.cafeapp.Admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MinumDatabase.Minum
import com.example.cafeapp.R
import com.example.cafeapp.databinding.TestItemMenuBinding
import java.io.File

class MinumAdminListAdapter(
    private var minumList: List<Minum>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MinumAdminListAdapter.MenuViewHolder>() {

    interface OnItemClickListener {
        fun onEditClick(item: Minum)
        fun onDeleteClick(item: Minum)
    }

    class MenuViewHolder(val binding: TestItemMenuBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TestItemMenuBinding.inflate(layoutInflater, parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val minum = minumList[position]
        holder.binding.apply {
            makanNama.text = minum.name
            makanHarga.text = "Rp. ${minum.harga}"
            makanDeskripsi.text = minum.deskripsi

            // Dapatkan path gambar dari direktori internal
            val context = holder.itemView.context
            val imgPath = File(context.filesDir, "app_images/${minum.namaFoto}")

            if (imgPath.exists()) {
                makanImage.setImageURI(Uri.fromFile(imgPath))
            } else {
                makanImage.setImageResource(R.drawable.placeholder_image)
            }

            // Set listener untuk tombol hapus dan item klik
            makanBtnHapus.setOnClickListener { listener.onDeleteClick(minum) }
            root.setOnClickListener { listener.onEditClick(minum) }
        }
    }

    override fun getItemCount() = minumList.size

    fun updateData(newMinumList: List<Minum>) {
        minumList = newMinumList
        notifyDataSetChanged()
    }
}
