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
    // Deklarasi variabel utama
    private lateinit var viewModel: CartViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: AddToCartAdapter

    // Fungsi untuk membuat tampilan fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inisialisasi binding dan ViewModel
        binding = FragmentCartBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]

        // Setup RecyclerView untuk menampilkan daftar item di keranjang
        setupRecyclerView()

        // Observasi perubahan data di ViewModel
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items) // Perbarui item di adapter
            updateTotalPrice(items) // Hitung ulang total harga
        }

        // Tombol Checkout
        binding.btnCheckout.setOnClickListener {
            if (viewModel.cartItems.value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Keranjang kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            navigateToTransaksiFragment() // Navigasi ke fragment transaksi
        }

        return binding.root
    }

    // Setup RecyclerView
    private fun setupRecyclerView() {
        adapter = AddToCartAdapter(mutableListOf(), viewModel, this) // Inisialisasi adapter
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context) // Gunakan layout manager linear
    }

    // Fungsi untuk memperbarui total harga
    private fun updateTotalPrice(items: List<CartItem>) {
        var totalPrice = 0
        for (item in items) {
            // Bersihkan format harga dari string
            val priceString = item.price
                .replace("Rp ", "")
                .replace(".0", "")
                .replace(",", "")
                .trim()

            // Ubah harga menjadi integer
            val itemPrice = try {
                priceString.toInt()
            } catch (e: NumberFormatException) {
                Log.e("CartFragment", "Error parsing price: $priceString", e)
                0
            }

            // Hitung total harga berdasarkan kuantitas
            totalPrice += itemPrice * item.quantity
        }

        // Tampilkan total harga ke TextView
        binding.tvTotalPrice.text = "Rp ${String.format("%,d", totalPrice)}"
    }

    // Fungsi untuk navigasi ke fragment transaksi
    private fun navigateToTransaksiFragment() {
        val cartItems = viewModel.cartItems.value ?: return

        // Hitung total harga
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

        // Siapkan data untuk navigasi
        val cartItemsArray = cartItems.toTypedArray()
        val bundle = Bundle().apply {
            putParcelableArray("cart_items", cartItemsArray)
            putInt("total_price", totalPrice)
        }

        // Navigasi ke transaksi fragment
        findNavController().navigate(R.id.action_cartFragment_to_transaksiFragment, bundle)

        // Kosongkan keranjang setelah navigasi
        viewModel.clearCart()
        CartManager.clear()
    }

    // Fungsi untuk memperbarui data saat fragment kembali aktif
    override fun onResume() {
        super.onResume()
        viewModel.refreshItems() // Segarkan item keranjang
    }

    // Fungsi callback untuk memperbarui total harga
    override fun onTotalPriceUpdated(totalPrice: Double) {
        updateTotalPrice(viewModel.cartItems.value ?: listOf())
    }
}