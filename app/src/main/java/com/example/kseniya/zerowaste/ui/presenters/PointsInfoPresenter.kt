package com.example.kseniya.zerowaste.ui.presenters

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.kseniya.zerowaste.adapters.PointsInfoAdapter
import com.example.kseniya.zerowaste.data.ReceptionPoint
import com.example.kseniya.zerowaste.interfaces.SortedList
import java.util.ArrayList

class PointsInfoPresenter {
    private lateinit var viewInterface: SortedList

    private var adapter: PointsInfoAdapter? = null
    private lateinit var items: List<ReceptionPoint>

    fun bind(viewInterface: SortedList,items: List<ReceptionPoint>) {
        this.items = items
        this.viewInterface = viewInterface
    }

    fun bindRecyclerView(recyclerView: RecyclerView) {
        adapter = PointsInfoAdapter( items, viewInterface)
        recyclerView.adapter = adapter

    }



    fun pointsForPosition(position: Int): ReceptionPoint = adapter!!.filterItems[position]
}