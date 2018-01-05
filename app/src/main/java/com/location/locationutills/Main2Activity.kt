package com.location.locationutills

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.location.locationutills.databinding.ActivityMain2Binding
import java.text.SimpleDateFormat
import java.util.*

class Main2Activity : AppCompatActivity() {
    lateinit var mBinding: ActivityMain2Binding
    var gps1: GPSTracker = GPSTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        gps1 = GPSTracker
//
        mBinding = DataBindingUtil.setContentView(this@Main2Activity, R.layout.activity_main2)
//        mBinding.btn.setOnClickListener {
//
//            if (gps1.canGetLocation()) {
//                val latitude = gps1.getLatitude()
//                val longitude = gps1.getLongitude()
//                // \n is for new line
//                Toast.makeText(applicationContext, "Your Location is - \nLat: $latitude\nLong: $longitude", Toast.LENGTH_LONG).show()
//            } else {
//                gps1.showSettingsAlert()
//            }
//
//        }
//        mBinding.btn2.setOnClickListener {
//            gps1.stopUsingGPS()
//            Toast.makeText(applicationContext, "Stop", Toast.LENGTH_LONG).show()
//        }
//        mBinding.btn3.setOnClickListener {
//            gps1.startLocationUpdate(object : CustomLocationListener {
//                @SuppressLint("SimpleDateFormat")
//                override fun onLocationChage(mLocation: Location?) {
//
////                    mBinding.tvResultListener1.text = mLocation.toString()
//                    val date = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS").format(Date(mLocation!!.getTime()));
//                    Log.d("second", "onLocationChage: 2222222222222222" + date + "@@@@@@@@@" + mLocation)
//
////                    mBinding.tvResultListener2.text = date
//                    Toast.makeText(this@Main2Activity, "Location Listener Working..!!", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
//
//        //Attach multiple listener in single activity
//        mBinding.btn4.setOnClickListener {
//            if (gps1.canGetLocation()) {
//                val latitude = gps1.getLatitude()
//                val longitude = gps1.getLongitude()
//                Toast.makeText(applicationContext, "Your Location is -Part 2 \nLat: $latitude\nLong: $longitude", Toast.LENGTH_LONG).show()
//            } else {
//                gps1.showSettingsAlert()
//            }
//        }
//
//
//        mBinding.btn5.setOnClickListener {
//            gps1.stopUsingGPS()
//            Toast.makeText(applicationContext, "Part 2 ....Stop2", Toast.LENGTH_LONG).show()
//        }
//
//        mBinding.btn6.setOnClickListener {
//            gps1.startLocationUpdate(object : CustomLocationListener {
//                @SuppressLint("SimpleDateFormat")
//                override fun onLocationChage(mLocation: Location?) {
//
////                    mBinding.tvResultListener1.text = mLocation.toString()
//                    val date = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS").format(Date(mLocation!!.getTime()));
//                    Log.d("second", "onLocationChage...Part2: 2222222222222222$date@@@@@@@@@$mLocation")
//
////                    mBinding.tvResultListener2.text = date
//                    Toast.makeText(this@Main2Activity, " Part 2 Location Listener Working..!!", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
//    }
    }
}