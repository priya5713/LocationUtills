package com.location.locationutills

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import android.support.v4.app.FragmentActivity
import android.util.Log

class MapActivity : FragmentActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    lateinit var gps: GPSTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.isMyLocationEnabled = true
        gps = GPSTracker
        if (gps.canGetLocation()) {

            val lat = gps.getLatitude().toString()
            val lng = gps.getLongitude().toString()
            Log.d(TAG, "onMapReady: Map Lat Long" + lat + "***" + lng)

            // Add a marker in Sydney and move the camera
            val mLocation = LatLng(lat.toDouble(), lng.toDouble())
            mMap!!.addMarker(MarkerOptions().position(mLocation).title("Tutorialspoint.com"))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(mLocation))
        }
        gps.startLocationUpdate(object : CustomLocationListener {
            override fun onLocationChage(mLocation: Location?) {
                mMap?.isMyLocationEnabled = true
//                mMap?.clear()
                mMap?.addMarker(MarkerOptions().position(LatLng(mLocation!!.latitude, mLocation!!.longitude)))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLocation!!.latitude, mLocation!!.longitude)))

            }
        })
    }

    companion object {
        val TAG: String = "Map Activity"
    }
}
