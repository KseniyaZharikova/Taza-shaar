package com.example.kseniya.zerowaste.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.kseniya.zerowaste.R
import com.example.kseniya.zerowaste.interfaces.CheckBoxInterface
import com.example.kseniya.zerowaste.interfaces.SortedList
import com.example.kseniya.zerowaste.ui.activities.MainActivity

import kotlinx.android.synthetic.main.fragment_chose.*


class ChoseFragment : BaseFragment(), View.OnClickListener {
    var activity: MainActivity? = null
    private var mCallBack: CheckBoxInterface? = null

    override fun getViewLayout(): Int {
        return R.layout.fragment_chose
    }

    override fun onClick(v: View?) {
        switchFragment()
        val tag = v!!.tag as String?
        mCallBack!!.onCheckBoxClicked(tag!!.toInt())
    }


    private fun switchFragment() {
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, PointsInfoFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCheckbox()
        mCallBack!!.showAllPoints()
        SortedList.list.clear()
    }

    private fun initCheckbox() {
        checkboxBottle.setOnClickListener(this)
        checkboxGlass.setOnClickListener(this)
        checkboxPaper.setOnClickListener(this)
        checkboxShirt.setOnClickListener(this)
        checkboxBag.setOnClickListener(this)
        checkboxApple.setOnClickListener(this)
        checkboxCow.setOnClickListener(this)
        checkboxMashine.setOnClickListener(this)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallBack = context as CheckBoxInterface
    }
}