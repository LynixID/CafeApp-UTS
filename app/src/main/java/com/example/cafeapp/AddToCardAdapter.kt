package com.example.cafeapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class AddToCardAdapter(
    val items: MutableList<CartItem>,
    private val viewModel: CardViewModel,
    private val totalPriceListener: TotalPriceListener
) : RecyclerView.Adapter<AddToCardAdapter.CartViewHolder>() {

    // Interface definition for total price updates
    interface TotalPriceListener {
        fun onTotalPriceUpdated(totalPrice: Double)
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_to_card, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        // Set item details
        holder.itemName.text = item.name
        holder.itemPrice.text = "Rp ${item.price}"
        holder.itemQuantity.text = item.quantity.toString()

        // Load image using file path from internal storage
        val context = holder.itemView.context
        val imgPath = File(context.filesDir, "app_images/${item.imageResId}")

        if (imgPath.exists()) {
            holder.itemImage.setImageURI(Uri.fromFile(imgPath))
        } else {
            holder.itemImage.setImageResource(R.drawable.placeholder_image) // Gambar default
        }

        // Handle minus button click
        holder.btnMinus.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) { // Check if the position is still valid
                if (item.quantity > 1) {
                    val newQuantity = item.quantity - 1
                    item.quantity = newQuantity
                    holder.itemQuantity.text = newQuantity.toString()
                } else {
                    // Remove item if quantity becomes 0
                    items.removeAt(position)
                    viewModel.removeItem(item.id)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                }
                // Update total price after quantity is reduced
                totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
            }
        }

        // Handle plus button click
        holder.btnPlus.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) { // Check if the position is still valid
                val newQuantity = item.quantity + 1
                item.quantity = newQuantity
                holder.itemQuantity.text = newQuantity.toString()

                // Update total price after quantity is increased
                totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
            }
        }
    }

    override fun getItemCount() = items.size

    // Calculate total price of all items in cart
    private fun calculateTotalPrice(): Double {
        return items.sumOf {
            try {
                it.price.replace("Rp ", "").replace(",", "").trim().toDouble() * it.quantity
            } catch (e: NumberFormatException) {
                0.0 // Return 0 if price format is invalid
            }
        }
    }

    // Method to update items list
    fun updateItems(newItems: List<CartItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
        totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
    }

    // Method to add single item
    fun addItem(item: CartItem) {
        val existingItemIndex = items.indexOfFirst { it.id == item.id }
        if (existingItemIndex != -1) {
            // Update existing item quantity
            val existingItem = items[existingItemIndex]
            existingItem.quantity += item.quantity
            notifyItemChanged(existingItemIndex)
        } else {
            // Add new item
            items.add(item)
            notifyItemInserted(items.size - 1)
        }
        totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
    }
}
