package com.example.mowpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.activity.viewModels
import com.example.mowpro.databinding.ActivityMainBinding
import com.example.mowpro.viewmodels.WeatherCardViewModel
import com.example.mowpro.viewmodels.WeatherCardViewModelFactory

class MainActivity : AppCompatActivity() {

    val logTag = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private val weatherVM: WeatherCardViewModel by viewModels { WeatherCardViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainWeatherCard.apply {
            weatherVM.weatherTemp.observe(this@MainActivity) {
                weatherCurrentTemp.text = it
                d(logTag, (weatherCurrentTemp.text) as String)
            }
            weatherVM.weatherDegreesF.observe(this@MainActivity) {
                weatherDegreesF.text = it
                d(logTag, (weatherDegreesF.text) as String)
            }
            weatherVM.weatherDescription.observe(this@MainActivity) {
                weatherDescription.text = it
                d(logTag, (weatherDescription.text) as String)
            }

        }
    }
}