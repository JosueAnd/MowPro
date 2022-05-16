package com.example.mowpro.network

import com.squareup.moshi.Json

data class CurrentWeatherData(@Json(name = "weather") private val weatherDescriptors: List<Map<String, Any>>,
                              @Json(name = "main") private val weatherTemperatures: Map<String, Any>) {

    val weatherDescriptor: String = determineWeatherDescriptor("main")
    val weatherCurrentTemp: Double = determineWeatherTemp("temp")
    val weatherIconName: String = determineWeatherDescriptor("icon")

    private fun determineWeatherDescriptor(key:String): String {
        val descriptors = weatherDescriptors.firstOrNull()
        return descriptors?.get(key)?.toString() ?: ""
    }

    private fun determineWeatherTemp(key: String): Double {
        val temp = weatherTemperatures[key] ?: 0.0
        return if (temp is Number) temp.toDouble() else 0.0
    }
}


val fakeWeatherData = CurrentWeatherData(listOf(mapOf("main" to "Not Available",
                                                      "icon" to "01d")),
                                         mapOf("feels_like" to 0.0))