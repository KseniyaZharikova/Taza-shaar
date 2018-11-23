package com.example.kseniya.zerowaste.ui.fragments

import android.os.Bundle
import android.view.View
import com.example.kseniya.zerowaste.R
import kotlinx.android.synthetic.main.fragment_chose.*
import com.example.kseniya.zerowaste.ui.activities.MainActivity
import com.example.kseniya.zerowaste.utils.GestureListener


class ChoseFragment : BaseFragment(), GestureListener.Callback {
     var activity: MainActivity? = null

    var expandedContentYPos: Float = 0f
    var collapsedContentYPos: Float = 0f

    private var gestureListener: GestureListener? = null

    override fun getViewLayout(): Int {

        return R.layout.fragment_chose
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentView.setOnClickListener {
            gestureListener?.let {
                if (it.isCollapsed) {
                    expandView()
                } else {
                    collapseView()
                }
            }
        }
    }

    override fun clearProgressAnimations(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun animateCurrentStatus() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collapseView() {

        if (gestureListener?.isAnimating == false) {
            gestureListener?.isCollapsed = true

            contentView.animate()
                    .translationY(collapsedContentYPos)
                    .setListener(gestureListener?.contentAnimListener)
                    .start()
            collapsedView.alpha = 1f
            expandedView.alpha = 0f
            collapsedView.visibility = View.VISIBLE
            expandedView.visibility = View.GONE

            waitCarArrow .setImageResource(R.drawable.ic_launcher_background)


        }
    }

    override fun expandView() {
        if (gestureListener?.isAnimating == false) {
            gestureListener?.isCollapsed = false

            collapsedView.alpha = 0f
            expandedView.alpha = 1f
            collapsedView.visibility = View.GONE
            expandedView.visibility = View.VISIBLE

            contentView .animate()
                    .translationY(expandedContentYPos)
                    .setListener(gestureListener?.contentAnimListener)
                    .start()

            waitCarArrow .setImageResource(R.drawable.ic_launcher_background)


        }    }

    override fun changeAlpha(alpha: Float) {
        collapsedView .alpha = alpha
        expandedView .alpha = 1 - alpha    }

    override fun getY(): Float {
        return contentView .y    }

    override fun setY(y: Float) {
        contentView .y = y    }

    override fun onResume() {
        super.onResume()

    }

}