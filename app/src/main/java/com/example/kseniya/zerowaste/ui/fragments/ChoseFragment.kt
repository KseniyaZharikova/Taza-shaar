package com.example.kseniya.zerowaste.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.interfaces.CheckBoxInterface
import com.example.kseniya.zerowaste.interfaces.SortedList
import com.example.kseniya.zerowaste.ui.activities.MainActivity
import com.example.kseniya.zerowaste.utils.BitmapUtil
import com.example.kseniya.zerowaste.utils.GestureListener
import kotlinx.android.synthetic.main.fragment_chose.*


class ChoseFragment : BaseFragment(), GestureListener.Callback, View.OnClickListener {
    var activity: MainActivity? = null
    var expandedContentYPos: Float = 0f
    var collapsedContentYPos: Float = 0f

    private var gestureListener: GestureListener? = null
    private var mCallBack: CheckBoxInterface? = null

    override fun getViewLayout(): Int {
        return R.layout.fragment_chose
    }

    override fun onClick(v: View?) {
        switchFragment()
        val tag = v!!.tag as String?
        mCallBack!!.onCheckBoxClicked(tag!!.toInt())
    }


    private fun switchFragment() {
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, PointsInfoFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCheckbox()
        mCallBack!!.showAllPoints()
        contentView.setOnClickListener {
            gestureListener?.let {
                if (it.isCollapsed) {

                    expandView()
                } else {

                    collapseView()
                }
            }
        }
        SortedList.list.clear()
    }

    private fun initCheckbox() {
        checkboxBottle.setOnClickListener(this)
        checkboxGlass.setOnClickListener(this)
        checkboxPaper.setOnClickListener(this)
        checkboxShirt.setOnClickListener(this)
        checkboxBag.setOnClickListener(this)
        checkboxApple.setOnClickListener(this)
        checkboxCow.setOnClickListener(this)
        checkboxMashine.setOnClickListener(this)
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

        }
    }

    override fun expandView() {
        if (gestureListener?.isAnimating == false) {
            gestureListener?.isCollapsed = false

            collapsedView.alpha = 0f
            expandedView.alpha = 1f
            collapsedView.visibility = View.GONE
            expandedView.visibility = View.VISIBLE

            contentView.animate()
                    .translationY(expandedContentYPos)
                    .setListener(gestureListener!!.contentAnimListener)
                    .start()
        }
    }

    override fun changeAlpha(alpha: Float) {
        collapsedView.alpha = alpha
        expandedView.alpha = 1 - alpha
    }

    override fun getY(): Float {
        return contentView.y
    }

    override fun setY(y: Float) {
        contentView.y = y
    }

    override fun onResume() {
        super.onResume()
        contentView?.post {
            if (isUIAvailable() == true && gestureListener == null) {
                expandedContentYPos = contentView.y
                collapsedContentYPos = expandedContentYPos + expandedView.height - BitmapUtil.dp2px(context, 70)
                gestureListener = GestureListener(collapsedContentYPos, expandedContentYPos, this@ChoseFragment)
                contentView?.setOnTouchListener(gestureListener)
            }
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallBack = context as CheckBoxInterface
    }
}