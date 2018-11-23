package com.example.kseniya.zerowaste.data

data class ReceptionPoint(
        val id: String,
        val name: String,
        val address: String,
        val type: String,
        val price: String,
        val open_time: String,
        val close_time: String,
        val latitude: String,
        val longitude: String)