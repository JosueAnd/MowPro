package com.example.mowpro.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.mowpro.DEGREES_FAHRENHEIT
import com.example.mowpro.network.CurrentWeatherApi
import com.example.mowpro.network.CurrentWeatherApiStatus
import com.example.mowpro.network.CurrentWeatherData
import kotlinx.coroutines.launch

class WeatherCardViewModel: ViewModel() {

    private val logTag = "WeatherCardVM"

    private val _status = MutableLiveData<CurrentWeatherApiStatus>()
    val status: LiveData<CurrentWeatherApiStatus> = _status
    var temperature: LiveData<String> = Transformations.map(weatherData) { "${it.temperature.toInt()}" }
    var tempScale: LiveData<String> = Transformations.map(weatherData) { DEGREES_FAHRENHEIT }
    var description: LiveData<String> = Transformations.map(weatherData) { cwd ->
        val descriptors = cwd.description.split(" ")
        val newDescriptors = mutableListOf<String>()
        descriptors.forEach { desc ->
            newDescriptors.add(
                desc.first().uppercase().plus(
                    desc.slice(1 until desc.length).lowercase())
            )
        }
        newDescriptors.joinToString(" ")
    }

    init {
        viewModelScope.launch {
            _status.value = CurrentWeatherApiStatus.LOADING
            try {
                weatherData.value = CurrentWeatherApi.retrofitService
                    .getCurrentWeatherData(latitude = "25",
                                           longitude = "25")
                _status.value = CurrentWeatherApiStatus.DONE
            } catch (e: Exception) {
                Log.d(logTag, "Exception Caught in WeatherCardViewModel: $e")
                _status.value = CurrentWeatherApiStatus.ERROR
                weatherData = MutableLiveData<CurrentWeatherData>()
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