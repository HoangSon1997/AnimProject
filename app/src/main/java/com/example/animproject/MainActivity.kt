package com.example.animproject

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

val list = mutableListOf(R.drawable.image, R.drawable.image, R.drawable.image, R.drawable.image, R.drawable.image)

class MainActivity : AppCompatActivity(), ClickListener {

    private lateinit var customView: CustomView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private lateinit var imageViewSelected: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecycler()

    }


    override fun onBackPressed() {
        if (customView.isVisible) {
            customView.hideWithAnimation(imageViewSelected)
            return
        }
        super.onBackPressed()
    }

    private fun setUpRecycler() {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ImageAdapter()
        adapter.setData(list)
        adapter.setListener(this)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = adapter

        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or
                    ItemTouchHelper.DOWN or
                    ItemTouchHelper.START or
                    ItemTouchHelper.END,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val positionDragged = viewHolder.adapterPosition
                val positionTarget = target.adapterPosition
                Collections.swap(list, positionDragged, positionTarget)
                adapter.setData(list)
                adapter.notifyItemMoved(positionDragged, positionTarget)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        })

        helper.attachToRecyclerView(recyclerView)
    }

    override fun onClick(position: Int, view: View) {
        customView = findViewById(R.id.custom_view)
        customView.setImgMain(view as ImageView)
        customView.showImage(position, view)
        view.visibility = View.GONE
        imageViewSelected = view
    }
}