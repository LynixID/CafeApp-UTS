package com.example.cafeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class CartFragment : Fragment(), AddToCardAdapter.TotalPriceListener { // Implement listener interface

    private lateinit var viewModel: CardViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddToCardAdapter
    private lateinit var totalPriceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        totalPriceTextView = view.findViewById(R.id.tvTotalPrice)

        viewModel = ViewModelProvider(this).get(CardViewModel::class.java)

        // Initialize RecyclerView with the adapter
        adapter = AddToCardAdapter(mutableListOf(), viewModel, this) // Pass the listener
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe changes in cart items
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.items.clear() // Hapus jika daftar item harus direset dari awal
            adapter.items.addAll(items)
            // Tidak perlu notifyDataSetChanged() di sini jika perubahan hanya terkait kuantitas
            updateTotalPrice(items) // Update total price when items change
        }

        return view
    }

    // Implement TotalPriceListener method
    override fun onTotalPriceUpdated(totalPrice: Double) {
        totalPriceTextView.text = "Rp $totalPrice" // Update total price display
    }

    // Update total price based on current items
    private fun updateTotalPrice(items: List<CartItem>) {
        var totalPrice = 0.0
        for (item in items) {
            // Pastikan harga item tidak kosong sebelum mengonversi
            val itemPriceString = item.price.trim() // Ambil string harga dan trim whitespace
            val itemPriceDouble = if (itemPriceString.isNotEmpty()) {
                itemPriceString.toDouble()
            } else {
                0.0 // Nilai default jika harga kosong
            }
            totalPrice += itemPriceDouble * item.quantity // Calculate total price
        }
        totalPriceTextView.text = "Rp $totalPrice" // Display total price
    }
}