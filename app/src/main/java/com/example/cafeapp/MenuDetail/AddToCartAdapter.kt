package com.example.cafeapp.MenuDetail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Cart.CartViewModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.AddToCartBinding
import java.io.File

class AddToCartAdapter(
    private val items: MutableList<CartItem>,
    private val viewModel: CartViewModel,
    private val totalPriceListener: TotalPriceListener
) : RecyclerView.Adapter<AddToCartAdapter.CartViewHolder>() {

    interface TotalPriceListener {
        fun onTotalPriceUpdated(totalPrice: Double)
    }

    class CartViewHolder(private val binding: AddToCartBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemImage: ImageView = binding.itemImage
        val itemName: TextView = binding.itemName
        val itemPrice: TextView = binding.itemPrice
        val itemQuantity: TextView = binding.itemQuantity
        val btnMinus: ImageButton = binding.btnMinus
        val btnPlus: ImageButton = binding.btnPlus
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = AddToCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
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
        // Handle minus button click
        holder.btnMinus.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                if (item.quantity > 1) {
                    val newQuantity = item.quantity - 1
                    item.quantity = newQuantity
                    holder.itemQuantity.text = newQuantity.toString()
                } else {
                    // Remove item if quantity becomes 0
                    items.removeAt(position)
                    viewModel.removeItem(item.id)  // Pass only item.id here
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                }
                // Update total price after quantity is reduced
                totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
            }
        }

        // Handle plus button click
        holder.btnPlus.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                val newQuantity = item.quantity + 1
                item.quantity = newQuantity
                holder.itemQuantity.text = newQuantity.toString()

                // Update total price after quantity is increased
                totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
            }
        }
    }

    override fun getItemCount() = items.size

    private fun calculateTotalPrice(): Double {
        return items.sumOf {
            try {
                it.price.replace("Rp ", "").replace(",", "").trim().toDouble() * it.quantity
            } catch (e: NumberFormatException) {
                0.0 // Return 0 if price format is invalid
            }
        }
    }

    fun updateItems(newItems: List<CartItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
        totalPriceListener.onTotalPriceUpdated(calculateTotalPrice())
    }

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
