package com.example.cafeapp.Transaksi

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafeapp.R
import com.example.cafeapp.databinding.RiwayatTransaksiBinding
import java.io.File

// Adapter untuk menampilkan daftar transaksi pada RecyclerView
class TransaksiAdapter(private val transaksiList: List<Transaksi>) :
    RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    // ViewHolder untuk menghubungkan tampilan dengan elemen di layout
    class TransaksiViewHolder(private val binding: RiwayatTransaksiBinding) : RecyclerView.ViewHolder(binding.root) {
        val namaMenu: TextView = binding.tvNamaMenu
        val harga: TextView = binding.tvHarga
        val pay: TextView = binding.tvPay
        val kembalian: TextView = binding.tvKembalian
        val jumlahTransaksi: TextView = binding.tvJumlah
        val waktu: TextView = binding.tvTanggalDanWaktu
        val gambarMenu: ImageView = binding.ivGambarMenu
        val orderId: TextView = binding.tvOrderId
    }

    // Menangani pembuatan ViewHolder dan inflating layout item transaksi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val binding = RiwayatTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransaksiViewHolder(binding)
    }

    // Menangani pengikatan data transaksi ke dalam ViewHolder
    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val transaksi = transaksiList[position]

        // Menampilkan nama menu yang dipesan
        holder.namaMenu.text = transaksi.namaMenu
        // Menampilkan harga total transaksi
        holder.harga.text = "Total: Rp ${String.format("%,d", transaksi.pembayaran)}"
        // Menampilkan jumlah pembayaran
        holder.pay.text = "Pembayaran: Rp ${String.format("%,d", transaksi.pembayaran)}"
        // Menampilkan jumlah kembalian
        holder.kembalian.text = "Kembalian: Rp ${String.format("%,d", transaksi.kembalian)}"
        // Menampilkan waktu transaksi
        holder.waktu.text = "Tanggal: ${transaksi.waktu}"
        // Menyembunyikan tampilan jumlah transaksi karena tidak digunakan
        holder.jumlahTransaksi.visibility = View.GONE
        // Menampilkan ID pesanan
        holder.orderId.text = "#${String.format("%05d", transaksi.id)}"

        // Memuat gambar menu jika tersedia menggunakan Glide
        transaksi.imagePath?.let { imagePath ->
            val imgFile = File(holder.itemView.context.filesDir, "app_images/$imagePath")
            if (imgFile.exists()) {
                Glide.with(holder.itemView.context)
                    .load(Uri.fromFile(imgFile)) // Memuat gambar dari file
                    .placeholder(R.drawable.placeholder_image) // Placeholder jika gambar tidak ditemukan
                    .into(holder.gambarMenu) // Menampilkan gambar ke ImageView
            } else {
                // Menampilkan gambar default jika file tidak ada
                Glide.with(holder.itemView.context)
                    .load(R.drawable.placeholder_image) // Menampilkan gambar placeholder
                    .into(holder.gambarMenu)
            }
        }
    }

    // Menyediakan jumlah item dalam daftar transaksi
    override fun getItemCount(): Int = transaksiList.size
}