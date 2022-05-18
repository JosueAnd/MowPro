package com.example.mowpro.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.mowpro.DEGREES_FAHRENHEIT
import com.example.mowpro.network.CurrentWeatherApi
import com.example.mowpro.network.CurrentWeatherApiStatus
import kotlinx.coroutines.launch
import com.example.mowpro.network.CurrentWeatherData
import java.lang.IllegalArgumentException

class WeatherCardViewModel: ViewModel() {

    private val logTag = "WeatherCardVM"

    private val _status = MutableLiveData<CurrentWeatherApiStatus>()
    val status: LiveData<CurrentWeatherApiStatus> = _status

    private var currentWeatherData = MutableLiveData<CurrentWeatherData>()
    var weatherTemp: LiveData<String> = Transformations.map(currentWeatherData) { "${it.weatherCurrentTemp.toInt()}" }
    var weatherDegreesF: LiveData<String> = Transformations.map(currentWeatherData) { DEGREES_FAHRENHEIT }

    init {
        viewModelScope.launch {
            _status.value = CurrentWeatherApiStatus.LOADING
            try {
                currentWeatherData.value = CurrentWeatherApi.retrofitService
                    .getCurrentWeatherData(latitude = "25",
                                           longitude = "25")
                _status.value = CurrentWeatherApiStatus.DONE
            } catch (e: Exception) {
                Log.d(logTag, "Exception Caught in WeatherCardViewModel: $e")
                _status.value = CurrentWeatherApiStatus.ERROR
                currentWeatherData = MutableLiveData()
            }
        }
    }
}

class WeatherCardViewModelFactory: ViewModelProvider.Factory {

    private val logTag = "WeatherCardVMF"

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherCardViewModel::class.java)) {
            Log.d(logTag, "Factory created the ViewModel")
            @Suppress("UNCHECKED_CAST")
            return WeatherCardViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}