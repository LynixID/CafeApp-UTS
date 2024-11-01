package com.example.cafeapp.Admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.R
import com.example.cafeapp.databinding.TestItemMenuBinding
import java.io.File

class MakanAdminAdapter(
    private var makanList: List<Makan>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MakanAdminAdapter.MenuViewHolder>() {

    interface OnItemClickListener {
        fun onEditClick(item: Makan)
        fun onDeleteClick(item: Makan)
    }

    class MenuViewHolder(private val binding: TestItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(makan: Makan, listener: OnItemClickListener) {
            // Menampilkan nama dan harga makan
            binding.makanNama.text = makan.name
            binding.makanHarga.text = "Rp. ${makan.harga}" // Menambahkan simbol mata uang
            binding.makanDeskripsi.text = makan.deskripsi

            // Dapatkan path gambar dari direktori internal
            val context = binding.root.context
            val imgPath = File(context.filesDir, "app_images/${makan.namaFoto}")

            if (imgPath.exists()) {
                // Jika file gambar ada, set gambar ke ImageView
                binding.makanImage.setImageURI(Uri.fromFile(imgPath))
            } else {
                // Jika gambar tidak ditemukan, tampilkan gambar default
                binding.makanImage.setImageResource(R.drawable.placeholder_image)
            }

            // Set listener untuk klik item
            binding.makanBtnHapus.setOnClickListener {
                listener.onDeleteClick(makan)
            }
            binding.root.setOnClickListener {
                listener.onEditClick(makan) // Panggil callback saat item diklik
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = TestItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = makanList[position]
        holder.bind(makan, listener)
    }

    override fun getItemCount() = makanList.size

    fun updateData(newMakanList: List<Makan>) {
        makanList = newMakanList
        notifyDataSetChanged() // Beritahu adapter bahwa data telah berubah
    }
}
