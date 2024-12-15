package com.example.cafeapp.MenuDetail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafeapp.Cart.CartViewModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.AddToCartBinding
import java.io.File

// Adapter untuk menampilkan daftar item di keranjang belanja
class AddToCartAdapter(
    private val items: MutableList<CartItem>, // Daftar item dalam keranjang
    private val viewModel: CartViewModel, // ViewModel untuk pengelolaan data
    private val totalPriceListener: TotalPriceListener // Listener untuk pembaruan total harga
) : RecyclerView.Adapter<AddToCartAdapter.CartViewHolder>() {

    // Listener untuk memperbarui total harga
    interface TotalPriceListener {
        fun onTotalPriceUpdated(totalPrice: Double)
    }

    // ViewHolder untuk item keranjang belanja
    class CartViewHolder(private val binding: AddToCartBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemImage: ImageView = binding.itemImage
        val itemName: TextView = binding.itemName
        val itemPrice: TextView = binding.itemPrice
        val itemQuantity: TextView = binding.itemQuantity
        val btnMinus: ImageButton = binding.btnMinus
        val btnPlus: ImageButton = binding.btnPlus
    }

    // Membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = AddToCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    // Menghubungkan data item dengan ViewHolder
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        // Mengatur detail item
        holder.itemName.text = item.name
        holder.itemPrice.text = "Rp ${item.price}"
        holder.itemQuantity.text = item.quantity.toString()

        // Memuat gambar dari penyimpanan internal menggunakan Glide
        val context = holder.itemView.context
        val imgPath = File(context.filesDir, "app_images/${item.imageResId}")

        if (imgPath.exists()) {
            // Menggunakan Glide untuk memuat gambar
            Glide.with(context)
                .load(Uri.fromFile(imgPath)) // Memuat gambar dari file
                .placeholder(R.drawable.placeholder_image) // Placeholder jika gambar tidak ditemukan
                .into(holder.itemImage) // Menampilkan gambar pada ImageView
        } else {
            // Jika gambar tidak ada, tampilkan placeholder menggunakan Glide
            Glide.with(context)
                .load(R.drawable.placeholder_image) // Menampilkan gambar placeholder
                .into(holder.itemImage)
        }

        // Klik tombol minus untuk mengurangi kuantitas
        holder.btnMinus.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                if (item.quantity > 1) {
                    val newQuantity = item.quantity - 1
                    item.quantity = newQuantity
                    holder.itemQuantity.text = newQuantity.toString()
                } else {
                    // Hapus item jika kuantitas menjadi 0
                    items.removeAt(position)
                    viewModel.removeItem(item.id)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                }
                totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
            }
        }

        // Klik tombol plus untuk menambah kuantitas
        holder.btnPlus.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                val newQuantity = item.quantity + 1
                item.quantity = newQuantity
                holder.itemQuantity.text = newQuantity.toString()
                totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
            }
        }
    }

    // Mendapatkan jumlah item di keranjang
    override fun getItemCount() = items.size

    // Menghitung total harga dari semua item di keranjang
    private fun calculateTotalPrice(): Double {
        return items.sumOf {
            try {
                it.price.replace("Rp ", "").replace(",", "").trim().toDouble() * it.quantity
            } catch (e: NumberFormatException) {
                0.0 // Jika format harga tidak valid
            }
        }
    }

    // Memperbarui daftar item di keranjang
    fun updateItems(newItems: List<CartItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
        totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
    }

    // Menambahkan item baru ke keranjang
    fun addItem(item: CartItem) {
        val existingItemIndex = items.indexOfFirst { it.id == item.id }
        if (existingItemIndex != -1) {
            val existingItem = items[existingItemIndex]
            existingItem.quantity += item.quantity
            notifyItemChanged(existingItemIndex)
        } else {
            items.add(item)
            notifyItemInserted(items.size - 1)
        }
        totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
    }
}