package com.example.kseniya.zerowaste.ui.activities

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.example.kseniya.zerowaste.R
import android.support.annotation.StringRes
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.transition.Transition
import com.example.kseniya.zerowaste.data.ReceptionPoint
import com.example.kseniya.zerowaste.ui.fragments.InfoFragment


abstract class BaseActivity : AppCompatActivity() {
    protected abstract fun getViewLayout(): Int
    var destroyed = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getViewLayout())
        ButterKnife.bind(this)
    }

    protected fun replaceFragment(fragment: Fragment) {
        if (supportFragmentManager == null) return
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }


    protected fun replaceFragmentToInfo(point: ReceptionPoint) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, InfoFragment.newInstance(point))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    }


    override fun onDestroy() {
        super.onDestroy()
        destroyed = true
    }

    protected fun showSimpleAlert() {
        if (!isFinishing) {
            AlertDialog.Builder(this)
                    .setTitle(title)
                    .setCancelable(false)
                    .setMessage(getString(R.string.title_no_internet))
                    .setNegativeButton(getString(R.string.close)) { _, _ ->
                        finish()
                    }
                    .show()
        }
    }
}