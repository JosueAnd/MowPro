package com.example.mowpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mowpro.controllers.fillWeatherLayout
import com.example.mowpro.databinding.ActivityMainBinding
import com.example.mowpro.viewmodels.WeatherCardViewModel
import com.example.mowpro.viewmodels.WeatherCardViewModelFactory

class MainActivity : AppCompatActivity() {

    val logTag = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private val weatherVM: WeatherCardViewModel by viewModels { WeatherCardViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fillWeatherLayout(this, binding.mainWeatherCard, weatherVM)
    }
}