package com.example.cafeapp.Transaksi

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import java.io.File

class TransaksiAdapter(private val transaksiList: List<Transaksi>) :
    RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    class TransaksiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaMenu: TextView = view.findViewById(R.id.tvNamaMenu)
        val harga: TextView = view.findViewById(R.id.tvHarga)
        val pay: TextView = view.findViewById(R.id.tvPay)
        val kembalian: TextView = view.findViewById(R.id.tvKembalian)
        val jumlahTransaksi: TextView = view.findViewById(R.id.tvJumlah)
        val waktu: TextView = view.findViewById(R.id.tvTanggalDanWaktu)
        val gambarMenu: ImageView = view.findViewById(R.id.ivGambarMenu)
        val orderId: TextView = view.findViewById(R.id.tvOrderId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.riwayat_transaksi, parent, false)
        return TransaksiViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val transaksi = transaksiList[position]
        Log.d("TransaksiAdapter", "Binding transaction: $transaksi")

        // Display menu items with their quantities
        holder.namaMenu.text = transaksi.namaMenu // Will display the itemized list
        // Display total price
        holder.harga.text = "Total: Rp ${String.format("%,d", transaksi.pembayaran)}"
        // Display payment amount
        holder.pay.text = "Pembayaran: Rp ${String.format("%,d", transaksi.pembayaran)}"
        // Display change amount
        holder.kembalian.text = "Kembalian: Rp ${String.format("%,d", transaksi.kembalian)}"
        // Display date and time
        holder.waktu.text = "Tanggal: ${transaksi.waktu}"
        // Remove the total items display since we're showing itemized list
        holder.jumlahTransaksi.visibility = View.GONE
        // Set Order ID
        holder.orderId.text = "#${String.format("%05d", transaksi.id)}"

        // Load image if available
        transaksi.imagePath?.let { imagePath ->
            val imgFile = File(holder.itemView.context.filesDir, "app_images/$imagePath")
            if (imgFile.exists()) {
                holder.gambarMenu.setImageURI(Uri.fromFile(imgFile))
            } else {
                // Set default image if file doesn't exist
                holder.gambarMenu.setImageResource(R.drawable.placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = transaksiList.size
}