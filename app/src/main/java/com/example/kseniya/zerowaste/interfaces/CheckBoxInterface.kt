package com.example.kseniya.zerowaste.interfaces

import com.example.kseniya.zerowaste.data.ReceptionPoint

interface CheckBoxInterface {
    fun onCheckBoxClicked(tag: Int)
    fun showAllPoints()
    fun zoomCameraToMarker(item: ReceptionPoint)
}