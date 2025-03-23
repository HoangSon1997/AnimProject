package com.example.animproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import com.google.android.material.transition.MaterialSharedAxis

class FragmentA : Fragment() {
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById<ImageView>(R.id.imageView)
        imageView.setOnClickListener {
            openFragmentB(imageView)
        }
        parentFragmentManager.setFragmentResultListener("back_from_B", this) { _, result ->
            val data = result.getString("key")
            if (data == "returned") {
                Log.d("FragmentA", "Fragment B đã bị back!")

                // Thực hiện hành động cần thiết
                imageView.visibility = View.VISIBLE
            }
        }


    }

    override fun onResume() {
        super.onResume()
    }

    private fun openFragmentB(sharedView: View) {
        val fragmentB = FragmentB()

        parentFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(sharedView, sharedView.transitionName)
            .add(R.id.fragmentContainer, fragmentB)
            .hide(this)
            .addToBackStack(null)
            .commit()
        imageView.visibility = View.GONE
    }

}