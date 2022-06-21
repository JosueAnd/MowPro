package com.example.mowpro.controllers

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class CurrentLocation(activity: AppCompatActivity): LocationListener {
    private val logTag = "CurrentLocation"

    private val locationManager = ContextCompat.getSystemService(activity.applicationContext,
                                                                 LocationManager::class.java)
    private val PROVIDER = LocationManager.NETWORK_PROVIDER

    private val _latitude = MutableLiveData<Double>()
    private val _longitude = MutableLiveData<Double>()

    val latitude: LiveData<Double> = _latitude
    val longitude: LiveData<Double> = _longitude

    init {
        try {
            if (ContextCompat.checkSelfPermission(activity.applicationContext,
                                                  Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity,
                                                  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                  101)
            }
            if (locationManager != null) {
                locationManager.requestLocationUpdates(PROVIDER,
                                                       1800000.toLong(), // 30 minutes
                                                       8046.72.toFloat(), // 5 miles
                                                       this@CurrentLocation)
                queryLocation()
                Log.e(logTag, _latitude.value.toString())
                Log.e(logTag, _longitude.value.toString())
            }
        } catch (e: Exception) {
            Log.e(logTag, e.toString())
        }
    }

    private fun queryLocation() {
        locationManager?.getLastKnownLocation(PROVIDER).apply {
            this?.let { onLocationChanged(it) }
        }
        Log.d(logTag, "location queried")
    }

    override fun onLocationChanged(location: Location) {
        _latitude.value = location.latitude
        _longitude.value = location.longitude
    }

    override fun onProviderDisabled(provider: String) {
        if (provider == PROVIDER) {
            Log.d(logTag, "provider disabled")
        }
    }

    override fun onProviderEnabled(provider: String) {
        if (provider == PROVIDER) {
            queryLocation()
            Log.d(logTag, "provider enabled")
        }
    }
}