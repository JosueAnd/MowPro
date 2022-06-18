package com.example.mowpro.network

import com.squareup.moshi.Json

class CurrentWeatherData(@Json(name = "weather") private val weatherDescriptors: List<Map<String, Any>>,
                         @Json(name = "main") private val weatherTemperatures: Map<String, Any>) {

//    val weatherDescriptor: String = determineWeatherDescriptor("main")
//    val weatherIconName: String = determineWeatherDescriptor("icon")
    private val _weatherDescription: String = determineWeatherDescriptor("description")
    val weatherDescription: String get() = _weatherDescription

    private val _weatherCurrentTemp: Double = determineWeatherTemp("temp")
    val weatherCurrentTemp: Double get() = _weatherCurrentTemp

    private fun determineWeatherDescriptor(key: String): String {
        val descriptors = weatherDescriptors.firstOrNull()
        return descriptors?.get(key)?.toString() ?: ""
    }

    private fun determineWeatherTemp(key: String): Double {
        val temp = weatherTemperatures[key] ?: 0.0
        return if (temp is Number) temp.toDouble() else 0.0
    }
}