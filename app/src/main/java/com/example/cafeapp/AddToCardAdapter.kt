package com.example.cafeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide

class AddToCardAdapter(
    private val context: Context,
    private val cartItems: List<item_addToCard>) : RecyclerView.Adapter<AddToCardAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val itemPrice: TextView = itemView.findViewById(R.id.makan_harga)
        val itemQuantity: TextView = itemView.findViewById(R.id.item_quantity)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btn_minus)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btn_plus)

        // Fungsi untuk meng-update jumlah item
        fun bind(item: item_addToCard) {
            itemName.text = item.name
            itemPrice.text = item.price
            itemQuantity.text = item.quantity.toString()
            // Anda dapat menggunakan Glide untuk memuat gambar dari resource ID
            //Glide.with(itemView).load(item.imageResId).into(itemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.add_to_card, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item)

        // Tambahkan listener untuk tombol plus dan minus
        holder.btnPlus.setOnClickListener {
            item.quantity++
            holder.itemQuantity.text = item.quantity.toString()
        }

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                holder.itemQuantity.text = item.quantity.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
