package com.example.cafeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.databinding.FragmentCartBinding

class CartFragment : Fragment(), AddToCardAdapter.TotalPriceListener {

    private lateinit var viewModel: CardViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: AddToCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        binding = FragmentCartBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(CardViewModel::class.java)

        // Initialize RecyclerView with the adapter
        adapter = AddToCardAdapter(mutableListOf(), viewModel, this) // Pass the listener
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe changes in cart items
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items) // Update items in adapter
            updateTotalPrice(items) // Update total price when items change
        }

        return binding.root // Return the root view from binding
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshItems()
    }

    // Implement TotalPriceListener method
    override fun onTotalPriceUpdated(totalPrice: Double) {
        binding.tvTotalPrice.text = "Rp $totalPrice" // Update total price display
    }

    // Update total price based on current items
    private fun updateTotalPrice(items: List<CartItem>) {
        var totalPrice = 0.0
        for (item in items) {
            val itemPrice = item.price.replace("Rp ", "").replace(",", "").trim().toDoubleOrNull() ?: 0.0
            totalPrice += itemPrice * item.quantity // Calculate total price
        }
        binding.tvTotalPrice.text = "Rp $totalPrice" // Display total price
    }
}
