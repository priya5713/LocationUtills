package com.location.locationutills

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.location.locationutills.databinding.ActivityMain2Binding
import java.text.SimpleDateFormat
import java.util.*

class Main2Activity : AppCompatActivity() {
    lateinit var mBinding: ActivityMain2Binding
    lateinit var listener1: CustomLocationListener
    lateinit var listener2: CustomLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this@Main2Activity, R.layout.activity_main2)
        mBinding.btn.setOnClickListener {

            if (GPSTracker.canGetLocation()) {
                val latitude = GPSTracker.getLatitude()
                val longitude = GPSTracker.getLongitude()
                // \n is for new line
                Toast.makeText(applicationContext, "Your Location is - \nLat: $latitude\nLong: $longitude", Toast.LENGTH_LONG).show()
            } else {
                GPSTracker.showSettingsAlert()
            }

        }
        mBinding.btn2.setOnClickListener {
            GPSTracker.stopUsingGPS(listener1)
            Log.d("Stop", "onCreate: STOP1")

            Toast.makeText(applicationContext, "Stop", Toast.LENGTH_LONG).show()
        }
        mBinding.btn3.setOnClickListener {
            listener1 = object : CustomLocationListener {
                @SuppressLint("SimpleDateFormat")
                override fun onLocationChage(mLocation: Location?) {

//                    mBinding.tvResultListener1.text = mLocation.toString()
                    val date = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS").format(Date(mLocation!!.getTime()));
                    Log.d("second", "onLocationChage:Part 1" + date + "@@@@@@@@@" + mLocation)

//                    mBinding.tvResultListener2.text = date
                    Toast.makeText(this@Main2Activity, "Location Listener Working..!!", Toast.LENGTH_SHORT).show()
                }
            }
            GPSTracker.startLocationUpdate(listener1)
        }

        //Attach multiple listener in single activity
        mBinding.btn4.setOnClickListener {
            if (GPSTracker.canGetLocation()) {
                val latitude = GPSTracker.getLatitude()
                val longitude = GPSTracker.getLongitude()
                Toast.makeText(applicationContext, "Your Location is -Part 2 \nLat: $latitude\nLong: $longitude", Toast.LENGTH_LONG).show()
            } else {
                GPSTracker.showSettingsAlert()
            }
        }


        mBinding.btn5.setOnClickListener {
            GPSTracker.stopUsingGPS(listener2)
            Log.d("stop", "onCreate: STOP2 ")

            Toast.makeText(applicationContext, "Part 2 ....Stop2", Toast.LENGTH_LONG).show()
        }

        mBinding.btn6.setOnClickListener {
            listener2 = object : CustomLocationListener {
                @SuppressLint("SimpleDateFormat")
                override fun onLocationChage(mLocation: Location?) {
//                    mBinding.tvResultListener1.text = mLocation.toString()
                    val date = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS").format(Date(mLocation!!.getTime()));
                    Log.d("second", "onLocationChage...Part2:$date@@@@@@@@@$mLocation")

                    Toast.makeText(this@Main2Activity, " Part 2 Location Listener Working..!!", Toast.LENGTH_SHORT).show()
                }
            }
            GPSTracker.startLocationUpdate(listener2)
        }
    }
}
