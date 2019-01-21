package com.example.kseniya.zerowaste.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.data.ReceptionPoint
import com.example.kseniya.zerowaste.interfaces.CheckBoxInterface
import com.example.kseniya.zerowaste.interfaces.SortedList
import com.example.kseniya.zerowaste.ui.presenters.PointsInfoPresenter
import com.example.kseniya.zerowaste.utils.BitmapUtil
import com.example.kseniya.zerowaste.utils.Constants
import com.example.kseniya.zerowaste.utils.GestureListener
import kotlinx.android.synthetic.main.fragment_points_info.*

class PointsInfoFragment : BaseFragment(),GestureListener.Callback, View.OnClickListener,SortedList{


    override fun setNoResultVisible(isEmpty: Boolean) {

    }
    var presenter = PointsInfoPresenter()
    lateinit var mCallBack : CheckBoxInterface
    var expandedContentYPos: Float = 0f
    var collapsedContentYPos: Float = 0f
    private var gestureListener: GestureListener? = null

    override fun getViewLayout(): Int {
        return R.layout.fragment_points_info
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back_arrow_button.setOnClickListener(this)
        val arrayTypes = resources.getStringArray(R.array.type_names)
        val nameType  = arrayTypes[SortedList.list[0].type.toInt()-1]
        titleTv.text = "Прием $nameType:"
        titleTv2.text = "Прием $nameType:"
        presenter.bind(this,SortedList.list)
        mCallBack.drawPointsByType()

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val params = contentView.layoutParams
                if (dy > 0) {

                    params.height = Constants.HIGHT_OF_ACTIVITY /2

                     expandView()
                    // Scrolling up
                } else {
                    // Scrolling down
                    params.height = Constants.HIGHT_OF_ACTIVITY
                    collapseView()

                }
                contentView.layoutParams = params
            }

        })

        recyclerView2.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val params = contentView.layoutParams
                if (dy > 0) {

                    params.height = Constants.HIGHT_OF_ACTIVITY

                    expandView()
                    // Scrolling up
                } else {
                    // Scrolling down
                    params.height = Constants.HIGHT_OF_ACTIVITY /2
                    collapseView()

                }
                contentView.layoutParams = params

            }


        })

        presenter.bindRecyclerView(recyclerView2)
        presenter.bindRecyclerView(recyclerView)

    }

    companion object {
        fun newInstance(title: String): PointsInfoFragment {
            val fragment = PointsInfoFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onClick(v: View?) {
        fragmentManager!!.popBackStack()
    }

    override fun onClickItem(position: Int) {
        val point = presenter.pointsForPosition(position)
       // DishListActivity.new(this, points.id, cafe.title)
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, InfoFragment.newInstance(point))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
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
                collapsedContentYPos = expandedContentYPos + expandedView.height - BitmapUtil.dp2px(context, 320)
                gestureListener = GestureListener(collapsedContentYPos, expandedContentYPos, this@PointsInfoFragment)
                contentView?.setOnTouchListener(gestureListener)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallBack = context as CheckBoxInterface
    }

}