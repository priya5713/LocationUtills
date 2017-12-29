package com.location.locationutills

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices

/**
 * Created by Inexture on 12/29/2017.
 */
class LocationProvider(mContext: Context) : android.location.LocationListener {

    lateinit var mContext: Context
    internal var isGPSEnabled = false
    internal var location: android.location.Location? = null // location
    internal var latitude: Double = 0.toDouble() // latitude
    internal var longitude: Double = 0.toDouble() // longitude


    init {
        getLocation()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {

            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (location == null) {

                    val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
                    @android.annotation.SuppressLint("MissingPermission") val lastLocation = mFusedLocationClient.lastLocation
                    lastLocation.addOnSuccessListener(this as java.util.concurrent.Executor, com.google.android.gms.tasks.OnSuccessListener { location ->
                        if (location != null) {
                            latitude = location.latitude
                            longitude = location.longitude
                        }
                    })

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location

    }

    override fun onLocationChanged(location: android.location.Location) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: android.os.Bundle) {}

    fun onBind(arg0: android.content.Intent): android.os.IBinder? {
        return null
    }

}