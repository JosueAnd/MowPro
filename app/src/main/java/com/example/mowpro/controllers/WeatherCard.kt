package com.example.mowpro.controllers

import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.mowpro.R
import com.example.mowpro.databinding.WeatherLayoutBinding
import com.example.mowpro.network.CurrentWeatherApiStatus
import com.example.mowpro.viewmodels.WeatherCardViewModel


fun fillWeatherLayout (activity: AppCompatActivity,
                      binding: WeatherLayoutBinding,
                      model: WeatherCardViewModel) {
    model.model.observe(activity) { vm ->
        binding.apply {
            vm.temperature.observe(activity) { temperature.text = it }
            vm.tempScale.observe(activity) { weatherScale.text = it }
            vm.description.observe(activity) { weatherDescription.text = it }
            vm.location.observe(activity) { weatherCityState.text = it }
            vm.iconLink.observe(activity) {
                when (vm.status.value) {
                    CurrentWeatherApiStatus.LOADING -> {
                        weatherIcon.setImageDrawable(activity.getDrawable(R.drawable.weather_pull_in_progress))
                    }
                    CurrentWeatherApiStatus.DONE -> {
                        weatherIcon.load(it)
                    }
                    CurrentWeatherApiStatus.ERROR -> {
                        weatherIcon.setImageDrawable(activity.getDrawable(R.drawable.weather_pull_fail))
                    }
                    else -> {
                        weatherIcon.setImageDrawable(activity.getDrawable(R.drawable.weather_pull_fail))
                    }
                }
            }
        }
    }
}