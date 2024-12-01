package com.example.cafeapp.Cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.MenuDetail.AddToCardAdapter
import com.example.cafeapp.MenuDetail.CartItem
import com.example.cafeapp.R
import com.example.cafeapp.databinding.FragmentCartBinding

class CartFragment : Fragment(), AddToCardAdapter.TotalPriceListener {

    private lateinit var viewModel: CardViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: AddToCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout using View Binding
        binding = FragmentCartBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity())[CardViewModel::class.java]

        // Initialize RecyclerView
        setupRecyclerView()

        // Observe cart items
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items)
            updateTotalPrice(items)
        }

        // Checkout button click listener
        binding.btnCheckout.setOnClickListener {
            if (viewModel.cartItems.value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Keranjang kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            navigateToTransaksiFragment()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = AddToCardAdapter(mutableListOf(), viewModel, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun navigateToTransaksiFragment() {
        val cartItems = ArrayList(viewModel.cartItems.value) // Copy cart items
        val totalPrice = binding.tvTotalPrice.text.toString().replace("Rp", "").replace(",", "").trim().toDouble()

        val bundle = Bundle().apply {
            putParcelableArrayList("cart_items", cartItems)
            putDouble("total_price", totalPrice)
        }

        findNavController().navigate(R.id.action_cartFragment_to_transaksiFragment, bundle)
    }

    // Update total price based on current cart items
    private fun updateTotalPrice(items: List<CartItem>) {
        var totalPrice = 0.0
        for (item in items) {
            val itemPrice = item.price.replace("Rp ", "").replace(",", "").trim().toDoubleOrNull() ?: 0.0
            totalPrice += itemPrice * item.quantity
        }
        binding.tvTotalPrice.text = "Rp ${String.format("%,.2f", totalPrice)}"
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshItems()
    }

    override fun onTotalPriceUpdated(totalPrice: Double) {
        binding.tvTotalPrice.text = "Rp ${String.format("%,.2f", totalPrice)}"
    }
}
