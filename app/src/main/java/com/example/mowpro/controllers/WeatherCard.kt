package com.example.mowpro.controllers

import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.mowpro.R
import com.example.mowpro.databinding.WeatherLayoutBinding
import com.example.mowpro.network.CurrentWeatherApiStatus
import com.example.mowpro.viewmodels.WeatherCardViewModel


class WeatherCard (activity: AppCompatActivity,
                   binding: WeatherLayoutBinding,
                   model: WeatherCardViewModel) {
    init {
        model.model.observe(activity) { vm ->
            when (vm.status.value) {
                CurrentWeatherApiStatus.LOADING, CurrentWeatherApiStatus.DONE -> {
                    binding.apply {
                        vm.temperature.observe(activity) { temperature.text = it }
                        vm.tempScale.observe(activity) { weatherScale.text = it }
                        vm.description.observe(activity) { weatherDescription.text = it }
                        vm.iconLink.observe(activity) { weatherIcon.load(it) }
                    }
                }
                CurrentWeatherApiStatus.ERROR -> {
                    binding.apply {
                        temperature.text = "-"
                        weatherScale.text = "-"
                        weatherDescription.text = "-"
                        weatherIcon.setImageDrawable(activity.getDrawable(R.drawable.weather_pull_fail))
                    }
                }
                else -> {}
            }
        }
    }
}