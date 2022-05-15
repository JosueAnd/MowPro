package com.example.mowpro.network

import com.example.mowpro.CURRENT_WEATHER_API_PATH
import com.example.mowpro.CURRENT_WEATHER_API_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(CURRENT_WEATHER_API_URL)
    .build()

interface CurrentWeatherApiService {

    /**
     * Expected fields for the query.
     * appid (required): String 'API Key'
     * lat (required): +/- Float -> String
     * lon (required): +/- Float -> String
     * units: String 'imperial'
     */
    @GET(CURRENT_WEATHER_API_PATH)
    suspend fun getCurrentWeatherData(@QueryMap params: Map<String, String>): CurrentWeatherData

}

object CurrentWeatherApi {

    val retrofitService: CurrentWeatherApiService by lazy {
        retrofit.create(CurrentWeatherApiService::class.java)
    }

}