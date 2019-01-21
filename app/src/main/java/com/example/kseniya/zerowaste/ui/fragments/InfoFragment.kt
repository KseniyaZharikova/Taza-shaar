package com.example.kseniya.zerowaste.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.data.ReceptionPoint
import com.example.kseniya.zerowaste.interfaces.CheckBoxInterface
import com.example.kseniya.zerowaste.utils.Constants
import kotlinx.android.synthetic.main.info_fragment.*
import kotlinx.android.synthetic.main.item_point_info.*


class InfoFragment: BaseFragment(), View.OnClickListener {

    var mCallBack: CheckBoxInterface? = null
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
        back_arrow_button_info.setOnClickListener(this)
        val params = view.layoutParams
        Log.d("layoutParams",  params.height.toString())
        params.height = (Constants.HIGHT_OF_ACTIVITY / 2) - 100
        view.layoutParams = params


        item = arguments!!.getSerializable("item") as ReceptionPoint

        mCallBack!!.zoomCameraToMarker(item!!)
       tvName.visibility =  View.GONE
        card_title.text = item!!.name
        tvAddress.text = item!!.address
        tvPhone.text = item!!.phone
        tvWorkTime.text = item!!.work_time
        tvPrice.text = item!!.price

    }

    override fun onClick(v: View?) {
        fragmentManager!!.popBackStack()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallBack = context as CheckBoxInterface
    }

}