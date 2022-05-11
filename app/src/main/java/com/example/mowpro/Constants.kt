package com.example.mowpro

import android.content.res.Resources

private val API_KEY = Resources.getSystem().getString(R.string.api_key)

const val CURRENT_WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?"
val CURRENT_WEATHER_TEST_PARAMS = "appid=$API_KEY&lat=28.432590&lon=-81.238850&units=imperial"