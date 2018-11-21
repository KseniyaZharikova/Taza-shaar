package com.example.kseniya.zerowaste.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife

abstract class BaseActivity: AppCompatActivity() {
    protected abstract fun getViewLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getViewLayout())
        ButterKnife.bind(this)
    }


}