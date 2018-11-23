package com.example.kseniya.zerowaste.utils

import android.animation.Animator
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import java.util.ArrayList

class GestureListener(private val collapsedContentYPos: Float, private val expandedContentYPos: Float, var callback: Callback) : View.OnTouchListener {
    private var velocityTracker: VelocityTracker? = null
    var isCollapsed: Boolean = false
    var isAnimating: Boolean = false

    private var moveYValue: Int = 0
    private var downYValue: Int = 0
    private val moveYPercentStep: Float = (collapsedContentYPos - expandedContentYPos) / 100
    private val contentCenter: Float = (collapsedContentYPos - expandedContentYPos) / 2
    internal var velocyPool: MutableList<Float> = ArrayList()

    internal val averageVelocity: Float
        get() {
            val sum = velocyPool.sum()
            return sum / velocyPool.size
        }

    var contentAnimListener: Animator.AnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            isAnimating = true
        }

        override fun onAnimationEnd(animation: Animator) {
            isAnimating = false

        }

        override fun onAnimationCancel(animation: Animator) {
            isAnimating = false
        }

        override fun onAnimationRepeat(animation: Animator) {

        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val y = event.rawY.toInt()

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downYValue = y - callback.getY().toInt()
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain()
                } else {
                    velocityTracker!!.clear()
                }
                velocyPool.clear()
                velocityTracker!!.addMovement(event)
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker!!.clear()

                val velocityY = averageVelocity

                val topEdgeY = callback.getY() - expandedContentYPos
                if (velocityY > 50 || velocityTracker!!.yVelocity > 50) {
                    callback.collapseView()
                } else if (velocityY < -50 || velocityTracker!!.yVelocity < -50) {
                    callback.expandView()
                } else {
                    if (topEdgeY > contentCenter) {
                        callback.collapseView()
                    } else {
                        callback.expandView()
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                moveYValue = y - downYValue
                if (!isAnimating && moveYValue >= expandedContentYPos && moveYValue <= collapsedContentYPos) {
                    velocityTracker!!.addMovement(event)
                    velocityTracker!!.computeCurrentVelocity(1000)
                    velocyPool.add(velocityTracker!!.yVelocity)

                    callback.setY(moveYValue.toFloat())

                    val alpha = Math.abs(moveYValue / moveYPercentStep) / 100
                    callback.changeAlpha(alpha)
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                velocityTracker!!.clear()
                velocityTracker!!.recycle()
                velocityTracker = null
            }
        }

        return true
    }

    interface Callback {
        fun collapseView()
        fun expandView()
        fun changeAlpha(alpha: Float)
        fun getY(): Float
        fun setY(y: Float)
    }
}