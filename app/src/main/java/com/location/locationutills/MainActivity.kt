package com.location.locationutills

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.util.Log
import com.location.locationutills.databinding.ActivityMainBinding
import android.widget.Toast
import android.os.Bundle
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    lateinit var listener: CustomLocationListener

    @SuppressLint("SetTextI18n")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationTracker.initTracker(this@MainActivity)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        checkPermissions(arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        // show location button click event
        mBinding.btn.setOnClickListener {

            // check if GPS enabled
            if (LocationTracker.getLastKnownLocation ()) {
                val latitude = LocationTracker.getLatitude()
                val longitude = LocationTracker.getLongitude()
                mBinding.tvResult.text = "lat :" + latitude.toString() + "long :" + longitude.toString()
                // \n is for new line
                Toast.makeText(applicationContext, "Your Location is - \nLat: $latitude\nLong: $longitude", Toast.LENGTH_LONG).show()
            } else {
                LocationTracker.showSettingsAlert()
            }
        }

        mBinding.btnStop.setOnClickListener {
            //            LocationTracker.stopUsingGPS(listener)
            LocationTracker.removeListener(listener)
            Log.d(TAG, "onCreate: Stop GPS")
        }


        mBinding.btnLastLocation.setOnClickListener {
            listener = object : CustomLocationListener {
                override fun onLocationChage(mLocation: Location?) {
                    Log.d(TAG, "onLocationChage: .............$mLocation")
                }
            }
            LocationTracker.addListener(listener)
        }
        mBinding.btnNext.setOnClickListener {
            val intent = Intent(this@MainActivity, Main2Activity::class.java)
            startActivity(intent)

        }
        mBinding.btnMap.setOnClickListener {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkPermissions(perms: ArrayList<String>) {

        val deniedPerms = arrayListOf<String>()
        val reqPerms = arrayListOf<String>()

        for (permission in perms) {
            if (ContextCompat.checkSelfPermission(this@MainActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permission)) {
                    deniedPerms.add(permission)
                } else {
                    reqPerms.add(permission)
                }
            }
        }
        if (deniedPerms.size > 0) {
            ActivityCompat.requestPermissions(this@MainActivity, deniedPerms.toTypedArray(), MY_PERMISSIONS_REQUEST_CODE)
        }
        if (reqPerms.size > 0) {
            ActivityCompat.requestPermissions(this@MainActivity, reqPerms.toTypedArray(), MY_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        for (i in 0 until permissions.size) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                LocationTracker.showSettingsAlert()
            }
        }
    }

    companion object {
        val MY_PERMISSIONS_REQUEST_CODE = 50

    }
}


