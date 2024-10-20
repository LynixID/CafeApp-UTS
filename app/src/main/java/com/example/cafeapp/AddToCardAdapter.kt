package com.example.cafeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        holder.itemPrice.text = item.price
        holder.itemImage.setImageResource(item.imageResId)
        holder.itemQuantity.text = item.quantity.toString()

        // Update total price on adapter creation
        totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())

        // Set click listener for minus button
        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity -= 1
                holder.itemQuantity.text = item.quantity.toString()
                viewModel.addItem(item) // Update item in ViewModel
            } else {
                viewModel.removeItem(item) // Remove item if quantity is 1
                items.removeAt(position)
                notifyItemRemoved(position)
            }
            totalPriceListener.onTotalPriceUpdated(calculateTotalPrice()) // Update total price
        }

        // Set click listener for plus button
        holder.btnPlus.setOnClickListener {
            item.quantity += 1
            holder.itemQuantity.text = item.quantity.toString()
            viewModel.addItem(item) // Update item in ViewModel
            totalPriceListener.onTotalPriceUpdated(calculateTotalPrice()) // Update total price
        }
    }

    override fun getItemCount() = items.size

    // Calculate total price based on items in the cart
    private fun calculateTotalPrice(): Double {
        return items.sumOf { it.price.toDouble() * it.quantity } // Assuming price is in String format
    }
}
