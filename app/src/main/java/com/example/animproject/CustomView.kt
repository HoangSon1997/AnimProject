package com.example.animproject

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.ViewCompat

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imgView: ImageView
    private var startBounds: Rect? = null
    private var startScaleX: Float = 1f
    private var startScaleY: Float = 1f

    init {
        LayoutInflater.from(context).inflate(R.layout.view_zoom_image, this, true)
        imgView = findViewById(R.id.imageViewDetail)

        visibility = View.GONE // Ẩn view khi mới tạo

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showImage(startView: ImageView) {
        visibility = View.VISIBLE
        post {
            startBounds = Rect()
            val finalBounds = Rect()

            startView.getGlobalVisibleRect(startBounds)
            this.getGlobalVisibleRect(finalBounds)

            val startWidth = startBounds!!.width().toFloat()
            val startHeight = startBounds!!.height().toFloat()
            val finalWidth = finalBounds.width().toFloat()
            val finalHeight = finalBounds.height().toFloat()

            // Kiểm tra tránh chia cho 0
            if (startWidth <= 0 || startHeight <= 0 || finalWidth <= 0 || finalHeight <= 0) {
                return@post
            }

            // Tính toán scale để khớp với ảnh ban đầu
            startScaleX = startWidth / finalWidth
            startScaleY = startHeight / finalHeight

            // Đặt ảnh vào đúng vị trí ảnh gốc
            pivotX = 0f
            pivotY = 0f
            scaleX = startScaleX
            scaleY = startScaleY
            translationX = startBounds!!.left.toFloat() - finalBounds.left
            translationY = startBounds!!.top.toFloat() - finalBounds.top

            // Animate để mở rộng ảnh
            animate()
                .scaleX(1f).scaleY(1f)
                .translationX(0f).translationY(0f)
                .setDuration(500)
                .setInterpolator(DecelerateInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        visibility = View.VISIBLE // Đảm bảo hiển thị khi mở
                    }
                })
                .start()
        }
    }

    fun hideWithAnimation(imgMain: ImageView) {
        animate()
        .scaleX(startScaleX).scaleY(startScaleY)
            .translationX(startBounds!!.left.toFloat() - left)
            .translationY(startBounds!!.top.toFloat() - top)
            .setDuration(500)
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    post {
                        visibility = View.GONE
                        scaleX = 1f
                        scaleY = 1f
                        translationX = 0f
                        translationY = 0f
                        imgMain.visibility = View.VISIBLE
                    }
                }
            })
            .start()
    }
}