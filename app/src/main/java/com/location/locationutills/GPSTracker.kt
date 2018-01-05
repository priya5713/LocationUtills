package com.location.locationutills

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.content.Intent
import android.os.Bundle
import android.app.AlertDialog
import android.content.DialogInterface
import android.location.LocationManager
import android.location.LocationListener
import android.provider.Settings
import android.util.Log
import android.content.ContentValues.TAG
import android.content.Context.LOCATION_SERVICE
import com.google.android.gms.location.*

@SuppressLint("Registered", "StaticFieldLeak")
object GPSTracker : LocationListener {

    private lateinit var mContext: Context
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    // The minimum distance to change Updates in meters
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

    // The minimum time between updates in milliseconds
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute

    // flag for GPS status
    private var isGPSEnabled = false
    private lateinit var listener: CustomLocationListener
    //    private var locationCallback: LocationCallback? = null
    private val UPDATE_INTERVAL = 1000 * 5
    private val FASTEST_INTERVAL = 1000 * 5
    private var locationRequest: LocationRequest? = LocationRequest()
    private var mStoredCallbackList: ArrayList<LocationCallback> = arrayListOf()
    private var mCusromStoredCallbackList: ArrayList<CustomLocationListener> = arrayListOf()

    // flag for network status
    internal var isNetworkEnabled = false
    // flag for GPS status
    private var canGetLocation = false
    private var location: Location? = null // location
    private var latitude: Double = 0.toDouble() // latitude
    private var longitude: Double = 0.toDouble() // longitude

    // Declaring a Location Manager
    lateinit var locationManager: LocationManager

    //    private lateinit var listener: CustomLocationListener
    fun initTracker(context: Context) {
        this.mContext = context
        Log.d(TAG, "initTracker: init tracker....")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        getLocation()
    }

    /**
     * It Returns lat long using FusedLocationClient
     * */
    @SuppressLint("MissingPermission")
    private fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true
                if (isNetworkEnabled) {

                    Log.d("Network", "Network")
                    if (true) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                        requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this)

                        if (location != null) {

                            val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext!!)
                            @android.annotation.SuppressLint("MissingPermission") val lastLocation = mFusedLocationClient.lastLocation
                            lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    latitude = location.latitude
                                    longitude = location.longitude
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    /**
     *It provides continuous updates of location using location listener
     * @return location
     * */

    @SuppressLint("MissingPermission")
    fun startLocationUpdate(listener: CustomLocationListener) {

        if (location != null) {

            locationRequest?.interval = UPDATE_INTERVAL.toLong()
            locationRequest?.fastestInterval = FASTEST_INTERVAL.toLong()
            locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    Log.d(TAG, "onLocationResult: .................." + location!!.longitude + "....." + location!!.latitude + "........" + location!!.time)
                    for (location in locationResult!!.locations) {
                        listener.onLocationChage(location)
                    }
                }
            }

            mStoredCallbackList.add(locationCallback)
            mCusromStoredCallbackList.add(listener)
            Log.d(TAG, "onLocationResult: Temp$mStoredCallbackList..........$mCusromStoredCallbackList")
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    fun stopUsingGPS(removeListener: CustomLocationListener) {

        locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
//        here we created two arraylist ,1st one is for custom location listener callback and 2nd one is for manual callback
//        listener ,bcz we get listener from custom location and we have to remove from manual listener so both
//        stored in a array and find the index of each ,using index we can remove listener...
        val mListenerIndex = mCusromStoredCallbackList.indexOf(removeListener)

        if (locationManager != null) {
            mFusedLocationClient.removeLocationUpdates(mStoredCallbackList[mListenerIndex])
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
            this.canGetLocation = true
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


}

private fun requestLocationUpdates(networK_PROVIDER: String, miN_TIME_BW_UPDATES: Long, miN_DISTANCE_CHANGE_FOR_UPDATES: Long, gpsTracker: GPSTracker) {}
