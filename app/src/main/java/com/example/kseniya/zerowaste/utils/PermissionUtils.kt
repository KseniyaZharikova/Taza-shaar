package com.example.kseniya.zerowaste.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

class PermissionUtils {
    companion object {
        fun isLocationEnable(activity: Activity): Boolean {

            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            if (isLocationGranted(activity)) return true

            ActivityCompat.requestPermissions(activity, permissions, Constants.LOCATION_REQUEST_CODE)

            return false
        }


         fun isLocationGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }

    }


}