package com.example.kseniya.zerowaste.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.data.ReceptionPoint

class PointsInfoAdapter(private val myDataset: List<ReceptionPoint>) : RecyclerView.Adapter<PointsInfoAdapter.MyViewHolder>() {

    class MyViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val textView= v.findViewById<TextView>(R.id.tvName)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsInfoAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_point_info, parent, false))
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.textView.text = myDataset[position].address
    }


    override fun getItemCount() = myDataset.size

}

