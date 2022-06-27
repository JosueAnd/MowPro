package com.example.mowpro.controllers

import android.Manifest
import android.content.pm.PackageManager
import android.location.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*


class CurrentLocation(activity: AppCompatActivity, listener: LocationListener) {
    private val logTag = "CurrentLocation"

    private val context = activity.applicationContext
    private val locationManager = ContextCompat.getSystemService(context, LocationManager::class.java)
    private val locationProvider = LocationManager.NETWORK_PROVIDER

    private val _latitude = MutableLiveData<Double>()
    private val _longitude = MutableLiveData<Double>()
    private val _city = MutableLiveData<String>()
    private val _state = MutableLiveData<String>()

    val latitude: LiveData<Double> = _latitude
    val longitude: LiveData<Double> = _longitude
    val city: LiveData<String> = _city
    val state: LiveData<String> = _state

    init {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity,
                                                  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                  101)
            }
            if (locationManager != null) {
                locationManager.requestLocationUpdates(locationProvider,
                                                       5000.toLong(), // 5 seconds
                                                       8046.72.toFloat(), // 5 miles
                                                       listener)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                                       1800000.toLong(), // 30 minutes
                                                       8046.72.toFloat(), // 5 miles
                                                       listener)
                update()
            }
        } catch (e: Exception) {
            Log.e(logTag, e.toString())
        }
    }

    fun update(location: Location? = locationManager?.getLastKnownLocation(locationProvider))
    {
        location?.let {
            _latitude.value = it.latitude
            _longitude.value = it.longitude
            queryAddress()
        }
        Log.d(logTag, "location queried")
    }

    private fun queryAddress() {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (latitude.value != null && longitude.value != null) {
                val addresses: List<Address>? =
                    geocoder.getFromLocation(latitude.value!!, longitude.value!!, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    _city.value = addresses[0].locality
                    _state.value = addresses[0].adminArea
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d(logTag, "queryLocation: ${_latitude.value.toString()}")
        Log.d(logTag, "queryLocation: ${_longitude.value.toString()}")
        Log.d(logTag, "queryAddress:  ${_city.value}")
        Log.d(logTag, "queryAddress:  ${_state.value}")
    }
}