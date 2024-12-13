package com.example.cafeapp.Cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.MenuDetail.AddToCartAdapter
import com.example.cafeapp.MenuDetail.CartItem
import com.example.cafeapp.R
import com.example.cafeapp.databinding.FragmentCartBinding

class CartFragment : Fragment(), AddToCartAdapter.TotalPriceListener {
    private lateinit var viewModel: CartViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: AddToCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]
        setupRecyclerView()

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items)
            updateTotalPrice(items)
        }

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
        adapter = AddToCartAdapter(mutableListOf(), viewModel, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun updateTotalPrice(items: List<CartItem>) {
        var totalPrice = 0
        for (item in items) {
            // Debug log untuk melihat nilai price sebelum diproses
            Log.d("CartFragment", "Raw price: ${item.price}")

            val priceString = item.price
                .replace("Rp ", "")
                .replace(".0", "")
                .replace(",", "")
                .trim()

            // Debug log untuk melihat string setelah dibersihkan
            Log.d("CartFragment", "Cleaned price string: $priceString")

            val itemPrice = try {
                priceString.toInt()
            } catch (e: NumberFormatException) {
                Log.e("CartFragment", "Error parsing price: $priceString", e)
                0
            }

            // Debug log untuk melihat hasil perhitungan
            Log.d("CartFragment", "Item: ${item.name}, Price: $itemPrice, Quantity: ${item.quantity}")

            totalPrice += itemPrice * item.quantity
        }

        // Debug log untuk total akhir
        Log.d("CartFragment", "Final total price: $totalPrice")

        binding.tvTotalPrice.text = "Rp ${String.format("%,d", totalPrice)}"
    }

    private fun navigateToTransaksiFragment() {
        val cartItems = viewModel.cartItems.value ?: return

        // Gunakan total yang sudah dihitung sebelumnya
        var totalPrice = 0
        for (item in cartItems) {
            val priceString = item.price
                .replace("Rp ", "")
                .replace(".0", "")
                .replace(",", "")
                .trim()

            val itemPrice = try {
                priceString.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            totalPrice += itemPrice * item.quantity
        }

        Log.d("CartFragment", "Total price before navigation: $totalPrice")

        val cartItemsArray = cartItems.toTypedArray()
        val bundle = Bundle().apply {
            putParcelableArray("cart_items", cartItemsArray)
            putInt("total_price", totalPrice)
        }

        // Navigate first
        findNavController().navigate(R.id.action_cartFragment_to_transaksiFragment, bundle)

        // Then clear cart
        viewModel.clearCart()
        CartManager.clear()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshItems()
    }

    override fun onTotalPriceUpdated(totalPrice: Double) {
        updateTotalPrice(viewModel.cartItems.value ?: listOf())
    }
}