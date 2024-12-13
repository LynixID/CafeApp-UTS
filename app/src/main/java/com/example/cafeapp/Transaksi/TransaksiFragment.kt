package com.example.cafeapp.Transaksi

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cafeapp.MenuDetail.CartItem
import com.example.cafeapp.R
import com.example.cafeapp.databinding.FragmentTransaksiBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Fragment untuk menangani transaksi pembelian
class TransaksiFragment : Fragment() {
    private var _binding: FragmentTransaksiBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TransaksiDatabase
    private var totalPrice: Int = 0
    private lateinit var cartItems: ArrayList<CartItem>

    // Menangani pembuatan tampilan untuk fragment transaksi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Menangani logika setelah tampilan fragment transaksi dibuat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = TransaksiDatabase.getInstance(requireContext())
        cartItems = ArrayList() // Inisialisasi daftar keranjang kosong

        // Inisialisasi UI dasar
        binding.tvTotalLabel.text = "Total: Rp 0"
        binding.tvTotalAmount.text = "Rp 0"

        // Menavigasi ke riwayat transaksi
        binding.btnViewHistory.setOnClickListener {
            findNavController().navigate(R.id.action_transaksiFragment_to_riwayatTransaksiFragment)
        }

        // Mendapatkan argumen dan memperbarui UI jika tersedia
        arguments?.let { args ->
            val cartItemsArray = args.getParcelableArray("cart_items") as? Array<CartItem>
            if (cartItemsArray != null && cartItemsArray.isNotEmpty()) {
                cartItems = ArrayList(cartItemsArray.toList())
                totalPrice = args.getInt("total_price")

                binding.tvTotalLabel.text = "Total: Rp ${String.format("%,d", totalPrice)}"
                binding.tvTotalAmount.text = "Rp ${String.format("%,d", totalPrice)}"

                val firstItem = cartItems[0]
                val imgPath = File(requireContext().filesDir, "app_images/${firstItem.imageResId}")
                if (imgPath.exists()) {
                    binding.ivTransactionImage.setImageURI(Uri.fromFile(imgPath))
                }
            }
        }

        setupCompleteTransaction() // Menyiapkan transaksi selesai
    }

    // Menangani logika saat transaksi selesai
    private fun setupCompleteTransaction() {
        binding.btnCompleteTransaction.setOnClickListener {
            val pembayaranStr = binding.etPembayaran.text.toString()
            if (pembayaranStr.isEmpty()) {
                Toast.makeText(requireContext(), "Masukkan jumlah pembayaran!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pembayaran = pembayaranStr.replace(",", "").toIntOrNull() ?: 0
            if (pembayaran < totalPrice) {
                Toast.makeText(requireContext(), "Pembayaran kurang!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val kembalian = pembayaran - totalPrice
            binding.textViewKembalian.visibility = View.VISIBLE
            binding.textViewKembalian.text = "Rp ${String.format("%,d", kembalian)}"

            if (cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "Tidak ada item untuk diproses", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Membuat daftar item menu yang dipesan
            val itemizedList = cartItems.joinToString("\n") {
                "${it.name} ${it.quantity} items"
            }

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val transaksi = Transaksi(
                        id = 0,
                        namaMenu = itemizedList,
                        pembayaran = pembayaran,
                        jumlahTransaksi = cartItems.sumOf { it.quantity },
                        kembalian = kembalian,
                        waktu = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                        imagePath = cartItems.firstOrNull()?.imageResId
                    )

                    db.transaksiDao().insertTransaksi(transaksi)

                    withContext(Dispatchers.Main) {
                        // Reset data transaksi setelah selesai
                        cartItems.clear()
                        totalPrice = 0
                        binding.tvTotalLabel.text = "Total: Rp 0"
                        binding.tvTotalAmount.text = "Rp 0"
                        binding.etPembayaran.text.clear()
                        binding.textViewKembalian.text = ""
                        binding.textViewKembalian.visibility = View.GONE
                        binding.ivTransactionImage.setImageResource(R.drawable.placeholder_image) // Gambar default

                        Toast.makeText(requireContext(), "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_transaksiFragment_to_riwayatTransaksiFragment)
                    }
                } catch (e: Exception) {
                    Log.e("TransaksiFragment", "Error saving transaction", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Membersihkan binding saat fragment dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}