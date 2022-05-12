package com.example.mowpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mowpro.databinding.ActivityMainBinding
import com.example.mowpro.network.CurrentWeatherApi
import com.example.mowpro.network.CurrentWeatherData
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherData: CurrentWeatherData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runBlocking {
            weatherData = CurrentWeatherApi.retrofitService.getCurrentWeatherData(
                mapOf("appid" to OPEN_WEATHER_API_KEY,
                      "lon" to "25",
                      "lat" to "25",
                      "units" to "imperial")
            )
        }

        binding.mainWeatherCard.weatherCurrentTemp.text = weatherData.weatherCurrentTemp
    }
}