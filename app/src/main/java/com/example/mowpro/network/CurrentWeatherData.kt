package com.example.mowpro.network

import android.util.Log
import com.squareup.moshi.Json

class CurrentWeatherData(@Json(name = "weather") private val weatherDescriptors: List<Map<String, Any>>,
                         @Json(name = "main") private val weatherTemperatures: Map<String, Any>) {

//    val weatherDescriptor: String = determineWeatherDescriptor("main")
//    private val weatherCurrentTemp: Double = determineWeatherTemp(weatherTemperatures, "temp")
//    val weatherIconName: String = determineWeatherDescriptor("icon")

//    private val _weatherCurrentTemp = setTheCurrentTemp()
    val weatherCurrentTempInt: Double  = determineWeatherTemp("temp")

//    private fun determineWeatherDescriptor(from: List<Map<String, Any>>, key: String): String {
//        val descriptors = from.firstOrNull()
//        return descriptors?.get(key)?.toString() ?: ""
//    }

    private fun determineWeatherTemp(key: String): Double {
        val temp = weatherTemperatures[key] ?: 0.0
        return if (temp is Number) temp.toDouble() else 0.0
    }
}