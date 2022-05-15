package com.example.mowpro.network

import com.squareup.moshi.Json

data class CurrentWeatherData(
        @Json(name = "weather") private val weatherDescriptors: List<Map<String, Any>>,
        @Json(name = "main") private val weatherTemperatures: Map<String, Any>) {

    val weatherDescriptor: String? = determineWeatherDescriptor("main")
    val weatherCurrentTemp: Double? = determineWeatherTemp("feels_like")
    val weatherIconName: String? = determineWeatherIcon()

    private fun determineWeatherDescriptor(key:String): String? {
        val descriptors = weatherDescriptors.firstOrNull()
        return descriptors?.get(key)?.toString()
    }

    private fun determineWeatherIcon(): String? {
        val iconName = determineWeatherDescriptor("icon")
        return if (iconName != "") iconName else null
    }

    private fun determineWeatherTemp(key: String): Double? {
        val temp = weatherTemperatures.get(key)
        return if (temp is Number) temp.toDouble() else null
    }
}