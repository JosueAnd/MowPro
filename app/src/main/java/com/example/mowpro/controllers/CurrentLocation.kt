package com.example.mowpro.controllers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService


class CurrentLocation(activity: AppCompatActivity, context: Context): LocationListener {
    private val locationManager = getSystemService(context, LocationManager::class.java)

    init {
        try {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                                                  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                  101)
            }
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                                    0L,
                                                    0F,
                                                    this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLocation(): Location? {
        return locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    override fun onLocationChanged(location: Location) {}
}