package com.example.cafeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AddToCardAdapter(
    val items: MutableList<CartItem>,
    private val viewModel: CardViewModel,
    private val totalPriceListener: TotalPriceListener // New listener interface
) : RecyclerView.Adapter<AddToCardAdapter.CartViewHolder>() {

    interface TotalPriceListener {
        fun onTotalPriceUpdated(totalPrice: Double) // Method to be called to update total price
    }

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.item_image)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemPrice: TextView = view.findViewById(R.id.item_price)
        val itemQuantity: TextView = view.findViewById(R.id.item_quantity)
        val btnMinus: ImageButton = view.findViewById(R.id.btn_minus)
        val btnPlus: ImageButton = view.findViewById(R.id.btn_plus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_to_card, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemPrice.text = "Rp ${item.price}"
        holder.itemQuantity.text = item.quantity.toString()

        Glide.with(holder.itemImage.context)
            .load(item.imagePath)
            .placeholder(R.drawable.sample_image)
            .into(holder.itemImage)

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity -= 1
                holder.itemQuantity.text = item.quantity.toString()
                viewModel.addItem(item)
            } else {
                viewModel.removeItem(item)
                items.removeAt(position)
                notifyItemRemoved(position)
            }
            totalPriceListener.onTotalPriceUpdated(calculateTotalPrice()) // Update total price here
        }

        holder.btnPlus.setOnClickListener {
            item.quantity += 1
            holder.itemQuantity.text = item.quantity.toString()
            viewModel.addItem(item)
            totalPriceListener.onTotalPriceUpdated(calculateTotalPrice()) // Update total price here
        }
    }

    override fun getItemCount() = items.size

    // Calculate total price based on items in the cart
    private fun calculateTotalPrice(): Double {
        return items.sumOf { it.price.toDouble() * it.quantity } // Menghitung total harga
    }
}
