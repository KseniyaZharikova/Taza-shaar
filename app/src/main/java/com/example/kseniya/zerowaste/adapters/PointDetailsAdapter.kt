package com.example.kseniya.zerowaste.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.data.ReceptionPoint

class PointDetailsAdapter(private val myDataset: ReceptionPoint) : RecyclerView.Adapter<PointDetailsAdapter.MyViewHolder>() {

    var filterItems: ReceptionPoint? = null

    init {
        filterItems = myDataset

    }

    class MyViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val tvName = v.findViewById<TextView>(R.id.tvName)
        val tvAddress = v.findViewById<TextView>(R.id.tvAddress)
        val tvPhone = v.findViewById<TextView>(R.id.tvPhone)
        val tvWorkTime = v.findViewById<TextView>(R.id.tvWorkTime)
        val tvPrice = v.findViewById<TextView>(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointDetailsAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_points_info_details, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvName.visibility = View.GONE
        holder.tvAddress.text = filterItems!!.address
        holder.tvPhone.text = filterItems!!.phone
        holder.tvWorkTime.text = filterItems!!.work_time
        holder.tvPrice.text = filterItems!!.price
    }

    override fun getItemCount(): Int {
        return 1
    }

}

