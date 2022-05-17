package com.example.mowpro.network

import com.example.mowpro.CURRENT_WEATHER_API_PATH
import com.example.mowpro.CURRENT_WEATHER_API_URL
import com.example.mowpro.OPEN_WEATHER_API_KEY
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(CURRENT_WEATHER_API_URL)
    .build()

interface CurrentWeatherApiService {

    @GET(CURRENT_WEATHER_API_PATH)
    suspend fun getCurrentWeatherData(@Query("appid") api_key: String = OPEN_WEATHER_API_KEY,
                                      @Query("units") unit_type: String = "imperial",
                                      @Query("lat") latitude: String,
                                      @Query("lon") longitude: String): CurrentWeatherData
}

object CurrentWeatherApi {

    val retrofitService: CurrentWeatherApiService by lazy {
        retrofit.create(CurrentWeatherApiService::class.java)
    }
}