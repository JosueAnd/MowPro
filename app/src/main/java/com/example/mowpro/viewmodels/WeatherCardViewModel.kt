package com.example.mowpro.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.mowpro.WEATHER_ICON_API_URL
import com.example.mowpro.network.CurrentWeatherApi
import com.example.mowpro.network.CurrentWeatherApiStatus
import kotlinx.coroutines.launch
import com.example.mowpro.network.CurrentWeatherData
import com.example.mowpro.network.fakeWeatherData
import java.lang.IllegalArgumentException

class WeatherCardViewModel: ViewModel() {

    private val LogTag = "WeatherCardVM"

    private val _currentWeatherData = MutableLiveData<CurrentWeatherData>()
    val currentWeatherData: LiveData<CurrentWeatherData> = _currentWeatherData

    private val _status = MutableLiveData<CurrentWeatherApiStatus>()
    val status: LiveData<CurrentWeatherApiStatus> = _status

    init { getCurrentWeatherData() }

    private fun getCurrentWeatherData() {
        viewModelScope.launch {
            _status.value = CurrentWeatherApiStatus.LOADING
            try {
                _currentWeatherData.value = CurrentWeatherApi.retrofitService
                    .getCurrentWeatherData(latitude = "25",
                                           longitude = "25")
                _status.value = CurrentWeatherApiStatus.DONE
            } catch (e: Exception) {
                Log.d(LogTag, "Exception Caught: $e")
                _status.value = CurrentWeatherApiStatus.ERROR
                _currentWeatherData.value = fakeWeatherData
            }
            Log.d(LogTag, "viewModelScope.launch reached end")
            Log.d(LogTag, currentWeatherData.value.toString())
        }
        Log.d(LogTag, "Exited viewModelScope.launch")
    }

    fun getWeatherIconUrl(): String? {
        return currentWeatherData.value?.weatherIconName?.also {
            return "$WEATHER_ICON_API_URL$it@2x.png"
        }
    }
}

class WeatherCardViewModelFactory: ViewModelProvider.Factory {

    private val LogTag = "WeatherCardVMF"

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherCardViewModel::class.java)) {
            Log.d(LogTag, "Factory created the ViewModel")
            @Suppress("UNCHECKED_CAST")
            return WeatherCardViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}