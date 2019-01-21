package com.example.kseniya.zerowaste.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.adapters.PointsInfoAdapter
import com.example.kseniya.zerowaste.data.ReceptionPoint
import com.example.kseniya.zerowaste.interfaces.CheckBoxInterface
import com.example.kseniya.zerowaste.interfaces.SortedList
import com.example.kseniya.zerowaste.ui.presenters.PointsInfoPresenter
import com.example.kseniya.zerowaste.utils.BitmapUtil
import com.example.kseniya.zerowaste.utils.Constants
import com.example.kseniya.zerowaste.utils.GestureListener
import kotlinx.android.synthetic.main.fragment_points_info.*

class PointsInfoFragment : BaseFragment(), View.OnClickListener, SortedList {
    override fun onClickItem(position: Int) {

    }

    override fun onClick(v: View?) {

    }


    override fun setNoResultVisible(isEmpty: Boolean) {

    }

    var presenter = PointsInfoPresenter()
    lateinit var mCallBack: CheckBoxInterface
    private var mIsShowingCardHeaderShadow: Boolean = false
    var expandedContentYPos: Float = 0f
    var collapsedContentYPos: Float = 0f
    private var gestureListener: GestureListener? = null

    override fun getViewLayout(): Int {
        return R.layout.fragment_points_info
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lm = LinearLayoutManager(context)
        card_recyclerview.layoutManager = lm
        presenter.bind(this, SortedList.list!!)
        presenter.bindRecyclerView(card_recyclerview)
        card_recyclerview.addItemDecoration(DividerItemDecoration(context, lm.orientation))

        card_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                // Animate the shadow view in/out as the user scrolls so that it
                // looks like the RecyclerView is scrolling beneath the card header.
                val isRecyclerViewScrolledToTop = lm.findFirstVisibleItemPosition() == 0 && lm.findViewByPosition(0)!!.top == 0
                if (!isRecyclerViewScrolledToTop && !mIsShowingCardHeaderShadow) {
                    mIsShowingCardHeaderShadow = true
                    showOrHideView(cardview, true)
                } else if (isRecyclerViewScrolledToTop && mIsShowingCardHeaderShadow) {
                    mIsShowingCardHeaderShadow = false
                    showOrHideView(cardview, false)
                }
            }
        })

        nestedscrollview.overScrollMode = View.OVER_SCROLL_NEVER
        nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nsv, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == 0 && oldScrollY > 0) {
                // Reset the RecyclerView's scroll position each time the card
                // returns to its starting position.
                card_recyclerview.scrollToPosition(0)
                cardview.alpha = 1.0f
                mIsShowingCardHeaderShadow = false
            }
        })
    }


    private fun showOrHideView(view: View, shouldShow: Boolean) {
        view.animate().alpha(if (shouldShow) 1f else 0f)
                .setDuration(100).interpolator = DecelerateInterpolator()
    }


//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        back_arrow_button.setOnClickListener(this)
//        val arrayTypes = resources.getStringArray(R.array.type_names)
//        val nameType = arrayTypes[SortedList.list[0].type.toInt() - 1]
//        titleTv.text = "Прием $nameType:"
//        titleTv2.text = "Прием $nameType:"
//        presenter.bind(this, SortedList.list)
//        mCallBack.drawPointsByType()
//
//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val params = contentView.layoutParams
//                if (dy > 0) {
//
//                    params.height = Constants.HIGHT_OF_ACTIVITY
//
//                    expandView()
//                    // Scrolling up
//                } else {
//                    // Scrolling down
////                    params.height = Constants.HIGHT_OF_ACTIVITY
//                    val v: LinearLayoutManager = recyclerView.layoutManager!! as LinearLayoutManager
//                    if (v.findFirstVisibleItemPosition() == 0)
////                    Log.d(" dasdasdads", "sd" + v.findFirstVisibleItemPosition())
//                        collapseView()
//
//                }
//                contentView.layoutParams = params
//            }
//
//        })
//
//        recyclerView2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                val params = contentView.layoutParams
//                if (dy > 0) {
//
//                    params.height = Constants.HIGHT_OF_ACTIVITY
//
//                    expandView()
//                    // Scrolling up
//                } else {
//                    // Scrolling down
////                    params.height = Constants.HIGHT_OF_ACTIVITY
//                    collapseView()
//
//                }
//                contentView.layoutParams = params
//
//            }
//
//
//        })
//
//        presenter.bindRecyclerView(recyclerView2)
//        presenter.bindRecyclerView(recyclerView)
//
//    }
//
//    companion object {
//        fun newInstance(title: String): PointsInfoFragment {
//            val fragment = PointsInfoFragment()
//            val bundle = Bundle()
//            bundle.putString("title", title)
//            fragment.arguments = bundle
//            return fragment
//        }
//    }
//
//
//    override fun onClick(v: View?) {
//        fragmentManager!!.popBackStack()
//    }
//
//    override fun onClickItem(position: Int) {
//        val point = presenter.pointsForPosition(position)
//        // DishListActivity.new(this, points.id, cafe.title)
//        val fragmentManager = fragmentManager
//        val fragmentTransaction = fragmentManager!!.beginTransaction()
//        fragmentTransaction.replace(R.id.container, InfoFragment.newInstance(point))
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
//    }
//
//    override fun collapseView() {
//
//        if (gestureListener?.isAnimating == false) {
//            gestureListener?.isCollapsed = true
//
//            contentView.animate()
//                    .translationY(collapsedContentYPos)
//                    .setListener(gestureListener?.contentAnimListener)
//                    .start()
//            collapsedView.alpha = 1f
//            expandedView.alpha = 0f
//            collapsedView.visibility = View.VISIBLE
//            expandedView.visibility = View.GONE
//
//        }
//    }
//
//    override fun expandView() {
//        if (gestureListener?.isAnimating == false) {
//            gestureListener?.isCollapsed = false
//
//            collapsedView.alpha = 0f
//            expandedView.alpha = 1f
//            collapsedView.visibility = View.GONE
//            expandedView.visibility = View.VISIBLE
//
//            contentView.animate()
//                    .translationY(expandedContentYPos)
//                    .setListener(gestureListener!!.contentAnimListener)
//                    .start()
//        }
//    }
//
//    override fun changeAlpha(alpha: Float) {
//        collapsedView.alpha = alpha
//        expandedView.alpha = 1 - alpha
//    }
//
//    override fun getY(): Float {
//        return contentView.y
//    }
//
//    override fun setY(y: Float) {
//        contentView.y = y
//    }
//
//    override fun onResume() {
//        super.onResume()
//        contentView?.post {
//            if (isUIAvailable() == true && gestureListener == null) {
//                expandedContentYPos = contentView.y
//                collapsedContentYPos = expandedContentYPos + expandedView.height - BitmapUtil.dp2px(context, 45)
//                gestureListener = GestureListener(collapsedContentYPos, expandedContentYPos, this@PointsInfoFragment)
//                contentView?.setOnTouchListener(gestureListener)
//            }
//        }
//    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallBack = context as CheckBoxInterface
    }

}