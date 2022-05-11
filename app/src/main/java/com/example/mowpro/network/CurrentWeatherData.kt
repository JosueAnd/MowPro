package com.example.mowpro.network

import com.squareup.moshi.Json

data class CurrentWeatherData(
        @Json(name = "weather") val weatherDescriptor: List<Map<String, Any>>,
        @Json(name = "main") val weatherTemperatures: Map<String, Any>)