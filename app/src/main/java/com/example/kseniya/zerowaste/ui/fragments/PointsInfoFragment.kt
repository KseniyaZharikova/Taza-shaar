package com.example.kseniya.zerowaste.ui.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.adapters.PointsInfoAdapter
import com.example.kseniya.zerowaste.interfaces.SortedList
import kotlinx.android.synthetic.main.fragment_points_info.*

class PointsInfoFragment : BaseFragment() {
    private var mAdapter: PointsInfoAdapter? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    override fun getViewLayout(): Int {
        return R.layout.fragment_points_info
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLayoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = mLayoutManager;
        mAdapter = PointsInfoAdapter(SortedList.list)
        recyclerView!!.adapter = mAdapter

    }
}