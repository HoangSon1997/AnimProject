package com.example.animproject

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import kotlin.math.abs

@SuppressLint("ClickableViewAccessibility")
class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imgViewDetail: ImageView
    private lateinit var imgViewMain: ImageView
    private lateinit var containerView: FrameLayout
    private var startBounds: Rect? = null
    private var startScaleX: Float = 1f
    private var startScaleY: Float = 1f
    private var isAnimating = false
    private val originalX = 0f
    private val originalY = 0f

    fun setImgMain(imgMain: ImageView) {
        imgViewMain = imgMain
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_zoom_image, this, true)
        imgViewDetail = findViewById(R.id.imageViewDetail)
        containerView = findViewById(R.id.container)

        alpha = 0f
        visibility = View.INVISIBLE // Ẩn view khi mới tạo

        val originalX = imgViewDetail.x
        val originalY = imgViewDetail.y

        imgViewDetail.setOnTouchListener(object : View.OnTouchListener {
            private var dX = 0f
            private var dY = 0f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = view.x - event.rawX
                        dY = view.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> {
                        containerView.setBackgroundColor(context.getColor(android.R.color.transparent))
                        view.animate()
                            .x(event.rawX + dX)
                            .y(event.rawY + dY)
                            .setDuration(0)
                            .start()

                    }

                    MotionEvent.ACTION_UP -> {
                        if (abs(event.rawY + dY - originalY) > 500) {
                            hideWithAnimation(imgViewMain)
                        } else {
                            // Khi thả tay, ImageView trở về vị trí ban đầu
                            view.animate()
                                .x(originalX)
                                .y(originalY)
                                .setDuration(500) // 0.5 giây để trở về
                                .start()
                            containerView.setBackgroundColor(context.getColor(R.color.white))
                        }
                    }
                }
                return true
            }
        })

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showImage(position: Int, startView: ImageView) {
        if (isAnimating) return
        isAnimating = true

        imgViewDetail.setImageResource(list[position])
        visibility = View.VISIBLE
        containerView.setBackgroundColor(context.getColor(R.color.white))
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
                        alpha = 1f
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        isAnimating = false
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
                        visibility = View.INVISIBLE
                        alpha = 0f
                        scaleX = 1f
                        scaleY = 1f
                        translationX = 0f
                        translationY = 0f
                        imgMain.visibility = View.VISIBLE
                        imgViewDetail.animate().x(originalX).y(originalY).setDuration(0).start()
                    }
                }
            })
            .start()
    }
}