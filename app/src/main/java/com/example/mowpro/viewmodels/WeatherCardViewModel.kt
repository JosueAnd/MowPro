package com.example.mowpro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mowpro.OPEN_WEATHER_API_KEY
import com.example.mowpro.WEATHER_ICON_API_URL
import com.example.mowpro.network.CurrentWeatherApi
import com.example.mowpro.network.CurrentWeatherApiStatus
import kotlinx.coroutines.launch
import com.example.mowpro.network.CurrentWeatherData

class WeatherCardViewModel: ViewModel() {

    private val _currentWeatherData = MutableLiveData<CurrentWeatherData?>()
    val currentWeatherData: LiveData<CurrentWeatherData?> = _currentWeatherData

    private val _status = MutableLiveData<CurrentWeatherApiStatus>()
    val status: LiveData<CurrentWeatherApiStatus> = _status

    init { getCurrentWeatherData() }

    private fun getCurrentWeatherData() {
        viewModelScope.launch {
            _status.value = CurrentWeatherApiStatus.LOADING
            try {
                _currentWeatherData.value = CurrentWeatherApi.retrofitService
                    .getCurrentWeatherData(mapOf("appid" to OPEN_WEATHER_API_KEY,
                                                 "lon" to "25",
                                                 "lat" to "25",
                                                 "units" to "imperial"))
                _status.value = CurrentWeatherApiStatus.DONE
            } catch (e: Exception) {
                _status.value = CurrentWeatherApiStatus.ERROR
                _currentWeatherData.value = null
            }
        }
    }

    fun getWeatherIconUrl(): String? {
        return currentWeatherData.value?.weatherIconName?.also {
            return "$WEATHER_ICON_API_URL$it@2x.png"
        }
    }
}