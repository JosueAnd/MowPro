package com.example.mowpro.network

import com.squareup.moshi.Json

class CurrentWeatherData(@Json(name = "weather") private val descriptors: List<Map<String, Any>>?,
                         @Json(name = "main") private val temperatures: Map<String, Any>?) {

    private val _description: String? = retrieveDescriptor("description")
    private val _temperature: Double? = retrieveTemperature()
    private val _iconName: String? = retrieveDescriptor("icon")

    val description: String? get() = _description
    val temperature: Double? get() = _temperature
    val iconName: String? get() = _iconName

    private fun retrieveDescriptor(key: String): String? {
        val descriptors = descriptors?.firstOrNull()
        return descriptors?.get(key)?.toString()
    }

    private fun retrieveTemperature(): Double? {
        val temp = temperatures?.get("temp")
        return if (temp is Number) temp.toDouble() else null
    }
}