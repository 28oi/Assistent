package com.example.ts3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PointsAdapter(private val points: List<String>) : RecyclerView.Adapter<PointsAdapter.PointViewHolder>() {

    // ViewHolder для каждого элемента списка
    class PointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pointTextView: TextView = itemView.findViewById(R.id.pointTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_point, parent, false)
        return PointViewHolder(view)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        val point = points[position]
        holder.pointTextView.text = point

        // Устанавливаем описание для доступности
        holder.pointTextView.contentDescription = "Пункт: $point"
    }

    override fun getItemCount(): Int {
        return points.size
    }
}
