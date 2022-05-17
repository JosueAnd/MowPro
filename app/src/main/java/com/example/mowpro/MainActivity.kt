package com.example.mowpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mowpro.databinding.ActivityMainBinding
import com.example.mowpro.viewmodels.WeatherCardViewModel
import com.example.mowpro.viewmodels.WeatherCardViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val weatherVM: WeatherCardViewModel by viewModels { WeatherCardViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainWeatherCard.apply {
            weatherVM.weatherTemp.observe(this@MainActivity) { weatherCurrentTemp.text = it }
            weatherVM.weatherDegreesF.observe(this@MainActivity) { weatherDegreesF.text = it }
        }
    }
}