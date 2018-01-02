package com.location.locationutills

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.content.Intent
import android.os.IBinder
import android.os.Bundle
import android.app.AlertDialog
import android.app.Service
import android.content.DialogInterface
import android.location.LocationManager
import android.location.LocationListener
import android.provider.Settings
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import android.content.ContentValues.TAG
import com.google.android.gms.location.LocationResult


@SuppressLint("Registered")
/**
 * Created by Inexture on 12/29/2017.
 */
class GPSTracker(private val mContext: Context) : Service(), LocationListener {

    // flag for GPS status
    internal var isGPSEnabled = false

    private var locationCallback: LocationCallback? = null

    val UPDATE_INTERVAL = 1000 * 3
    val FASTEST_INTERVAL = 1000 * 3

    private var locationRequest: LocationRequest? = LocationRequest()

    // flag for network status
    internal var isNetworkEnabled = false

    // flag for GPS status
    internal var canGetLocation = false
    internal var location: Location? = null // location
    internal var latitude: Double = 0.toDouble() // latitude
    internal var longitude: Double = 0.toDouble() // longitude


    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    lateinit var listener: CustomLocationListener

    init {
        getLocation()
//        getLocationListener()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {
            locationManager = mContext
                    .getSystemService(LOCATION_SERVICE) as LocationManager

            // getting GPS status
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true
                if (isNetworkEnabled) {

                    Log.d("Network", "Network")
                    if (locationManager != null) {
                        location = locationManager!!
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                        locationManager!!.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this)

                        if (location != null) {
                            val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
                            @android.annotation.SuppressLint("MissingPermission") val lastLocation = mFusedLocationClient.lastLocation
                            lastLocation.addOnSuccessListener(this as java.util.concurrent.Executor, com.google.android.gms.tasks.OnSuccessListener { location ->
                                if (location != null) {


//                                    this.locationCallback = object : LocationCallback() {
//                                        override fun onLocationResult(locationResult: LocationResult?) {
//                                            super.onLocationResult(locationResult) // why? this. is. retarded. Android.
//                                            val currentLocation = locationResult!!.lastLocation
//                                                Log.d(TAG, "onLocationResult:Current Location" + currentLocation)
//
//                                        }
//                                    }

                                    latitude = location.latitude
                                    longitude = location.longitude
                                }
                            })
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdate(listener: CustomLocationListener) {
        if (location != null) {
            val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)

            locationRequest?.interval = UPDATE_INTERVAL.toLong()
            locationRequest?.fastestInterval = FASTEST_INTERVAL.toLong()
            locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    for (location in locationResult!!.locations) {
                        Log.d(TAG, "onLocationResult: .................." + location.longitude + "....." + location.latitude + "........" + location.time)
                        listener.onLocationChage(location)
                    }

                }
            }
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

//            lastLocation.addOnSuccessListener(this as java.util.concurrent.Executor, com.google.android.gms.tasks.OnSuccessListener { location ->
//                if (location != null) {
//
//                    locationRequest?.interval = UPDATE_INTERVAL.toLong()
//                    locationRequest?.fastestInterval = FASTEST_INTERVAL.toLong()
//                    locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//
//                    latitude = location.latitude
//                    longitude = location.longitude
//                    listener.onLocationChage(location)
//
//                }
//            })

        }
    }

//    @SuppressLint("MissingPermission")
//    fun getLocationListener(): Location? {
//        try {
//            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
//
//            // getting GPS status
//            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
//
//            // getting network status
//            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                // no network provider is enabled
//            } else {
//                this.canGetLocation = true
//                if (isNetworkEnabled) {
//
//                    Log.d("Network", "Network")
//                    if (locationManager != null) {
//                        location = locationManager!!
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//
//                        locationManager!!.requestLocationUpdates(
//                                LocationManager.NETWORK_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
//
//                        if (location != null) {
//                            val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
//                            @android.annotation.SuppressLint("MissingPermission") val lastLocation = mFusedLocationClient.lastLocation
//                            lastLocation.addOnSuccessListener(this as java.util.concurrent.Executor, com.google.android.gms.tasks.OnSuccessListener { location ->
//                                if (location != null) {
//
//                                    locationRequest?.interval = UPDATE_INTERVAL.toLong()
//                                    locationRequest?.fastestInterval = FASTEST_INTERVAL.toLong()
//                                    locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//
//                                    latitude = location.latitude
//                                    longitude = location.longitude
//                                    listener.onLocationChage(location)
//
//                                }
//                            })
//                        }
//                    }
//                }
//                // if GPS Enabled get lat/long using GPS Services
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return location
//    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GPSTracker)
        }
    }

    /**
     * Function to get latitude
     */

    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.latitude
        }

        // return latitude
        return latitude
    }

    /**
     * Function to get longitude
     */
    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.longitude
        }

        // return longitude
        return longitude
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     */
    fun canGetLocation(): Boolean {
        return this.canGetLocation
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     */
    fun getLocationListener(listener: CustomLocationListener): Boolean {
        listener.onLocationChage(location!!)
        return this.canGetLocation
    }


    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */

    fun showSettingsAlert() {

        val alertDialog = AlertDialog.Builder(mContext)

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings")

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        })

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        // Showing Alert Message
        alertDialog.show()
    }

    override fun onLocationChanged(location: Location) {
        val data = listener.onLocationChage(location)
        Log.d(TAG, "onLocationChanged: Time interval" + data)

    }

    override fun onProviderDisabled(provider: String) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    companion object {

        // The minimum distance to change Updates in meters
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute
    }

}

private fun LocationManager.requestLocationUpdates(networK_PROVIDER: String, miN_TIME_BW_UPDATES: Long, miN_DISTANCE_CHANGE_FOR_UPDATES: Long, gpsTracker: GPSTracker) {}
