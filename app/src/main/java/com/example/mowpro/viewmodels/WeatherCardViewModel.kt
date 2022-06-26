package com.example.mowpro.viewmodels

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.mowpro.DEGREES_FAHRENHEIT
import com.example.mowpro.WEATHER_ICON_URL
import com.example.mowpro.controllers.CurrentLocation
import com.example.mowpro.network.CurrentWeatherApi
import com.example.mowpro.network.CurrentWeatherApiStatus
import com.example.mowpro.network.CurrentWeatherData
import kotlinx.coroutines.launch

class WeatherCardViewModel(activity: AppCompatActivity) : ViewModel() {

    private val logTag = "WeatherCardVM"

    private val _status = MutableLiveData<CurrentWeatherApiStatus>()
    private var weatherData = MutableLiveData<CurrentWeatherData>()
    private val locationManager = CurrentLocation(activity)
    private val _location = MutableLiveData<String>()

    private val _model = MutableLiveData<WeatherCardViewModel>()
    val model: LiveData<WeatherCardViewModel> = _model

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
    val iconLink: LiveData<String> = Transformations.map(weatherData) { WEATHER_ICON_URL.format(it.iconName) }
    val location: LiveData<String> = _location

    private fun setCurrentLocation() { _location.value = "${locationManager.city.value}, ${locationManager.state.value}" }

    init {
        Log.d(logTag, locationManager.latitude.value.toString())
        Log.d(logTag, locationManager.longitude.value.toString())
        viewModelScope.launch {
            _status.value = CurrentWeatherApiStatus.LOADING
            try {
                weatherData.value = CurrentWeatherApi.retrofitService
                    .getCurrentWeatherData(latitude = locationManager.latitude.value.toString(),
                                           longitude = locationManager.longitude.value.toString())
                _status.value = CurrentWeatherApiStatus.DONE
                setCurrentLocation()
            } catch (e: Exception) {
                Log.d(logTag, "Exception Caught in WeatherCardViewModel: $e")
                _status.value = CurrentWeatherApiStatus.ERROR
                weatherData = MutableLiveData<CurrentWeatherData>()
            }
            _model.value = this@WeatherCardViewModel
        }
    }
}