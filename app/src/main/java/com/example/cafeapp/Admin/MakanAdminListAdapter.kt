package com.example.cafeapp.Admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.R
import com.example.cafeapp.databinding.TestItemMenuBinding
import java.io.File

class MakanAdminListAdapter(
    private var makanList: List<Makan>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MakanAdminListAdapter.MenuViewHolder>() {

    interface OnItemClickListener {
        fun onEditClick(item: Makan)
        fun onDeleteClick(item: Makan)
    }

    class MenuViewHolder(val binding: TestItemMenuBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TestItemMenuBinding.inflate(layoutInflater, parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val makan = makanList[position]
        holder.binding.apply {
            makanNama.text = makan.name
            makanHarga.text = "Rp. ${makan.harga}"
            makanDeskripsi.text = makan.deskripsi

            val context = holder.itemView.context
            val imgPath = File(context.filesDir, "app_images/${makan.namaFoto}")

            if (imgPath.exists()) {
                makanImage.setImageURI(Uri.fromFile(imgPath))
            } else {
                makanImage.setImageResource(R.drawable.placeholder_image)
            }

            makanBtnHapus.setOnClickListener { listener.onDeleteClick(makan) }
            root.setOnClickListener { listener.onEditClick(makan) }
        }
    }

    override fun getItemCount() = makanList.size

    fun updateData(newMenus: List<Makan>) {
        makanList = newMenus
        notifyDataSetChanged()
    }
}
