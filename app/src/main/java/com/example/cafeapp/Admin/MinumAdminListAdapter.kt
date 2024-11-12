package com.example.cafeapp.Admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MenuDatabase.Kategori
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.R
import com.example.cafeapp.databinding.TestItemMenuBinding
import java.io.File

class MinumAdminListAdapter(
    private val onItemClick: (Menu) -> Unit,
    private val onDeleteClick: (Menu) -> Unit
) : ListAdapter<Menu, MinumAdminListAdapter.MenuViewHolder>(MinumDiffCallback()) {

    // ViewHolder untuk menampilkan item menu
    class MenuViewHolder private constructor(val binding: TestItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): MenuViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TestItemMenuBinding.inflate(layoutInflater, parent, false)
                return MenuViewHolder(binding)
            }
        }

        // Bind data ke dalam item view
        fun bind(menu: Menu, onItemClick: (Menu) -> Unit, onDeleteClick: (Menu) -> Unit) {
            binding.makanNama.text = menu.nama
            binding.makanHarga.text = "Rp. ${menu.harga}"
            binding.makanDeskripsi.text = menu.deskripsi

            // Dapatkan path gambar dari direktori internal
            val context = binding.root.context
            val imgPath = File(context.filesDir, "app_images/${menu.namaFoto}")

            if (imgPath.exists()) {
                binding.makanImage.setImageURI(Uri.fromFile(imgPath))
            } else {
                binding.makanImage.setImageResource(R.drawable.placeholder_image)
            }

            // Set listener untuk klik item dan tombol hapus
            binding.root.setOnClickListener {
                onItemClick(menu)
            }
            binding.makanBtnHapus.setOnClickListener {
                onDeleteClick(menu)
            }
        }
    }

    // Membuat ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return when (viewType) {
            Kategori.MINUM.ordinal -> MenuViewHolder.from(parent)  // Jika kategori adalah MINUM
            else -> MenuViewHolder.from(parent)  // Jika kategori lain (misalnya MAKAN)
        }
    }

    // Mengikat data ke ViewHolder
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = getItem(position)  // Ambil data item menu di posisi tertentu
        when (holder) {
            is MenuViewHolder -> {
                // Ikat data menu ke MenuViewHolder
                holder.bind(menu, onItemClick, onDeleteClick)
            }
        }
    }

    // Menentukan tipe item berdasarkan kategori
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).kategori) {
            Kategori.MINUM -> Kategori.MINUM.ordinal
            else -> Kategori.MAKAN.ordinal
        }
    }

    // Kelas DiffUtil untuk membandingkan item
    class MinumDiffCallback : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem._id == newItem._id  // Membandingkan ID untuk identifikasi item
        }

        override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem == newItem  // Membandingkan seluruh data item
        }
    }

    // Fungsi untuk memperbarui data
    fun updateData(newMinumList: List<Menu>) {
        submitList(newMinumList)  // Mengirimkan daftar baru ke adapter untuk diperbarui
    }
}
