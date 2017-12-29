package com.location.locationutills

import android.content.ContentValues.TAG
import android.databinding.DataBindingUtil
import android.util.Log
import com.location.locationutills.databinding.ActivityMainBinding
import android.widget.Toast
import android.os.Bundle
import android.app.Activity
import android.view.View
import android.widget.Button


/**
 * Created by Inexture on 12/29/2017.
 */


class MainActivity : Activity() {

    internal lateinit var btnShowLocation: Button

    // GPSTracker class
    internal lateinit var gps: GPSTracker
    lateinit var mBinding: ActivityMainBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        // show location button click event
        mBinding.btn.setOnClickListener {

            // create class object
            gps = GPSTracker(this@MainActivity)

            // check if GPS enabled
            if (gps.canGetLocation()) {

                val latitude = gps.getLatitude()
                val longitude = gps.getLongitude()

                // \n is for new line
                Toast.makeText(applicationContext, "Your Location is - \nLat: $latitude\nLong: $longitude", Toast.LENGTH_LONG).show()
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert()
            }

        }

    }

}
