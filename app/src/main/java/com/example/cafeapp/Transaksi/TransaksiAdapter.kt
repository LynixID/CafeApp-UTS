package com.example.cafeapp.Transaksi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R

class TransaksiAdapter(private val transaksiList: List<Transaksi>) : RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.riwayat_transaksi, parent, false)
        return TransaksiViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val transaksi = transaksiList[position]
        holder.namaMenu.text = transaksi.namaMenu
        holder.jumlahTransaksi.text = "Jumlah: ${transaksi.jumlahTransaksi}"
    }

    override fun getItemCount(): Int = transaksiList.size

    class TransaksiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaMenu: TextView = view.findViewById(R.id.tvNamaMenu)
        val harga: TextView = view.findViewById(R.id.tvHarga)
        val jumlahTransaksi: TextView = view.findViewById(R.id.tvJumlah)
        val waktu: TextView = view.findViewById(R.id.tvTanggalDanWaktu)
    }
}
