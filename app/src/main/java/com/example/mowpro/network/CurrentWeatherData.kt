package com.example.mowpro.network

import com.squareup.moshi.Json
import kotlin.math.roundToInt

data class CurrentWeatherData(
        @Json(name = "weather") private val weatherDescriptors: List<Map<String, Any>>,
        @Json(name = "main") private val weatherTemperatures: Map<String, Any>) {

    val weatherDescriptor: String = determineWeatherDescriptor()
    val weatherCurrentTemp: Int = determineWeatherTemp("feels_like")

    private fun determineWeatherDescriptor(): String {
        if (weatherDescriptors.isEmpty())
            return "No Description Available"

        val descriptors = weatherDescriptors.first()
        if (descriptors.containsKey("main"))
            return descriptors.get("main").toString()
        return "No Description Available"
    }

    private fun determineWeatherTemp(key: String): Int {
        if (weatherTemperatures.isEmpty())
            return 0
        if (weatherTemperatures.get(key) is Number)
            return (weatherTemperatures.get(key) as Number).toDouble().roundToInt()
        return 0
    }
}