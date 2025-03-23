package com.example.animproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import kotlin.math.abs

class FragmentB : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var containerView: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)

        sharedElementReturnTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_b, container, false)
        containerView = view.findViewById(R.id.container)
        handle(view)
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handle(view: View) {
        imageView = view.findViewById(R.id.imageView)
        val originalX = imageView.x
        val originalY = imageView.y

        imageView.setOnTouchListener(object : View.OnTouchListener {
            private var dX = 0f
            private var dY = 0f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = view.x - event.rawX
                        dY = view.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> {
                        view.animate()
                            .x(event.rawX + dX)
                            .y(event.rawY + dY)
                            .setDuration(0)
                            .start()

                        containerView.setBackgroundColor(requireContext().getColor(android.R.color.transparent))
                    }

                    MotionEvent.ACTION_UP -> {
                        Log.d("sondeptrai", "onTouch: ${event.rawY} - $dY - $originalY")
                        if (abs(event.rawY + dY - originalY) > 500) {
                            activity?.supportFragmentManager?.popBackStack()
                        } else {
                            // Khi thả tay, ImageView trở về vị trí ban đầu
                            view.animate()
                                .x(originalX)
                                .y(originalY)
                                .setDuration(500) // 0.5 giây để trở về
                                .start()
                        }
                    }
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Gửi thông báo khi Fragment B bị back
        parentFragmentManager.setFragmentResult("back_from_B", bundleOf("key" to "returned"))
    }
}