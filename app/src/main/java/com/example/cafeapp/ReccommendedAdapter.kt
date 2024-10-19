package com.example.cafeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecommendedAdapter(private val itemList: List<RecommendedItem>) :
    RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended, parent, false)
        return RecommendedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)


    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewFood: ImageView = itemView.findViewById(R.id.imageViewFood)
        private val textViewFoodName: TextView = itemView.findViewById(R.id.textViewFoodName)
        private val textViewFoodDescription: TextView = itemView.findViewById(R.id.textViewFoodDescription)
        private val textViewFoodPrice: TextView = itemView.findViewById(R.id.textViewFoodPrice)


        fun bind(item: RecommendedItem) {
            textViewFoodName.text = item.name
            textViewFoodDescription.text = item.description
            textViewFoodPrice.text = item.price
            Glide.with(itemView.context)
                .load(item.imageResId) // Load drawable resource
                .into(imageViewFood)
        }
    }
}
