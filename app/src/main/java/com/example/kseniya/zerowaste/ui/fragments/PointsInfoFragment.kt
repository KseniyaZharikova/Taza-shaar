package com.example.kseniya.zerowaste.ui.fragments
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.interfaces.CheckBoxInterface
import com.example.kseniya.zerowaste.interfaces.SortedList
import com.example.kseniya.zerowaste.ui.presenters.PointsInfoPresenter
import kotlinx.android.synthetic.main.fragment_points_info.*

class PointsInfoFragment : BaseFragment(), View.OnClickListener, SortedList {


    var presenter = PointsInfoPresenter()
    lateinit var mCallBack: CheckBoxInterface
    private var mIsShowingCardHeaderShadow: Boolean = false
    override fun getViewLayout(): Int {
        return R.layout.fragment_points_info
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lm = LinearLayoutManager(context)
        card_recyclerview.layoutManager = lm
        val arrayTypes = resources.getStringArray(R.array.type_names)
        val nameType  = arrayTypes[SortedList.list[0].type.toInt()-1]
        card_title.text = "Прием $nameType:"
        go_back.setOnClickListener(this)
        presenter.bind(this, SortedList.list!!)
        presenter.bindRecyclerView(card_recyclerview)
        card_recyclerview.addItemDecoration(DividerItemDecoration(context, 0))

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


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallBack = context as CheckBoxInterface
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

    override fun onClick(v: View?) {
        fragmentManager!!.popBackStack()
    }


    override fun setNoResultVisible(isEmpty: Boolean) {

    }
}