package com.example.kseniya.zerowaste.ui.presenters

import com.example.kseniya.zerowaste.interfaces.ChooseInterface

class ChoosePresenter : ChooseInterface.Presenter, ChooseInterface.View {

    private var mView: ChooseInterface.View? = null



    override fun bind(view: ChooseInterface.View?) {
        mView = view
    }

    override fun unbind() {
        mView = null
    }

}