package com.example.kseniya.zerowaste.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.data.ReceptionPoint
import com.example.kseniya.zerowaste.interfaces.CheckBoxInterface
import com.example.kseniya.zerowaste.utils.BitmapUtil
import com.example.kseniya.zerowaste.utils.Constants
import com.example.kseniya.zerowaste.utils.GestureListener
import kotlinx.android.synthetic.main.info_fragment.*


class InfoFragment: BaseFragment(), GestureListener.Callback, View.OnClickListener {

    var expandedContentYPos: Float = 0f
    var collapsedContentYPos: Float = 0f
    var mCallBack: CheckBoxInterface? = null
    private var gestureListener: GestureListener? = null

    var item: ReceptionPoint? = null


    companion object {
        fun newInstance(item: ReceptionPoint): InfoFragment {
            val fragment = InfoFragment()
            val bundle = Bundle()
            bundle.putSerializable("item", item)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getViewLayout(): Int {
        return R.layout.info_fragment

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = view.layoutParams
        Log.d("layoutParams",  params.height.toString())
        params.height = (Constants.HIGHT_OF_ACTIVITY / 2) - 200
        view.layoutParams = params


        item = arguments!!.getSerializable("item") as ReceptionPoint
        contentView.setOnClickListener {
            gestureListener?.let {
                if (it.isCollapsed) {
                    expandView()
                } else {
                    collapseView()
                }
            }
        }

        mCallBack!!.zoomCameraToMarker(item!!)
        tvName.text = item!!.name
        tvAddress.text = item!!.address
        tvPhone.text = item!!.phone
        tvWorkTime.text = item!!.work_time
        tvPrice.text = item!!.price

    }

    override fun onClick(v: View?) {

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallBack = context as CheckBoxInterface
    }


    override fun onResume() {
        super.onResume()
        contentView?.post {
            if (isUIAvailable() == true && gestureListener == null) {
                expandedContentYPos = contentView.y
                collapsedContentYPos = expandedContentYPos + expandedView.height - BitmapUtil.dp2px(context, 40)
                gestureListener = GestureListener(collapsedContentYPos, expandedContentYPos, this@InfoFragment)
                contentView?.setOnTouchListener(gestureListener)
            }
        }
    }

}