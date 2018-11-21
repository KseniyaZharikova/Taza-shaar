package com.example.kseniya.zerowaste.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife

abstract class BaseFragment : Fragment() {
    protected abstract fun getViewLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate (getViewLayout(), container, false)
        ButterKnife.bind(this, view)
        return view
    }
}