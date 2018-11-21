package com.example.kseniya.zerowaste.fragments

import android.os.Bundle
import android.view.View
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.utils.GestureListener


class ChoseFragment : BaseFragment(), GestureListener.Callback  {
    private var gestureListener: GestureListener? = null
    override fun getViewLayout(): Int {
        return R.layout.fragment_chose
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun clearProgressAnimations(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun animateCurrentStatus() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collapseView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun expandView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeAlpha(alpha: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getY(): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setY(y: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()

    }

}