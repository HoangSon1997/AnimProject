package com.example.animproject

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.animate
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var customView: CustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (savedInstanceState == null) {
//            handleByFrag()
//        }

        handleByView()

    }

    private fun handleByView() {
        imageView = findViewById(R.id.imageView)
        customView = findViewById(R.id.custom_view)
        imageView.setOnClickListener {
            customView.showImage(imageView)
            imageView.visibility = View.GONE
        }

    }

    override fun onBackPressed() {
        if (customView.isVisible) {
            customView.hideWithAnimation(imageView)
//            customView.visibility = View.GONE
            return
        }
        super.onBackPressed()
    }


    private fun handleByFrag() {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, FragmentA())
//            .commit()
    }
}