package com.example.animproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter: RecyclerView.Adapter<ImageHolder>() {
    private val list = mutableListOf<Int>()
    private lateinit var listener: ClickListener

    fun setData(data: List<Int>) {
        list.clear()
        list.addAll(data)
    }

    fun setListener(clickListener: ClickListener) {
        listener = clickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ImageHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.imgItem.setImageResource(list[position])
        holder.imgItem.setOnClickListener {
            listener.onClick(position, holder.imgItem)
        }
    }
}

class ImageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val imgItem: ImageView = itemView.findViewById(R.id.imageViewDetail)
}

interface ClickListener {
    fun onClick(position: Int, view: View)
}