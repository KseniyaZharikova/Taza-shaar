package com.example.kseniya.zerowaste.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.data.ReceptionPoint
import com.example.kseniya.zerowaste.interfaces.SortedList

class PointsInfoAdapter(private val myDataset: List<ReceptionPoint>, private val viewInterface: SortedList) : RecyclerView.Adapter<PointsInfoAdapter.MyViewHolder>() {

    var filterItems: List<ReceptionPoint> = ArrayList()

    init {
        filterItems = myDataset
    }

    class MyViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val tvName= v.findViewById<TextView>(R.id.tvName)
        val tvAddress= v.findViewById<TextView>(R.id.tvAddress)
        val tvPhone= v.findViewById<TextView>(R.id.tvPhone)
        val tvWorkTime= v.findViewById<TextView>(R.id.tvWorkTime)
        val tvPrice= v.findViewById<TextView>(R.id.tvPrice)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsInfoAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_point_info, parent, false))
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = filterItems[position]
        holder.tvName.text = item.name + " " + SortedList.list.size
        holder.tvAddress.text = item.address
        holder.tvPhone.text = item.phone
        holder.tvWorkTime.text = item.work_time
        holder.tvPrice.text = item.price
        holder.itemView.setOnClickListener { viewInterface.onClickItem(position) }

    }


    override fun getItemCount(): Int {
        return filterItems.size
    }

}

