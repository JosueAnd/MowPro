package com.example.mowpro.viewmodels

import android.location.Location
import android.location.LocationListener
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

class WeatherCardViewModel(activity: AppCompatActivity) : ViewModel(), LocationListener {

    private val logTag = "WeatherCardVM"

    private val _status = MutableLiveData<CurrentWeatherApiStatus>()
    private val weatherData = MutableLiveData<CurrentWeatherData>()
    private val currentLocation = CurrentLocation(activity, this)

    private val _model = MutableLiveData<WeatherCardViewModel>()
    val model: LiveData<WeatherCardViewModel> = _model

    val status: LiveData<CurrentWeatherApiStatus> = _status
    var tempScale: LiveData<String> = Transformations.map(weatherData) { DEGREES_FAHRENHEIT }
    val iconLink: LiveData<String> = Transformations.map(weatherData) { WEATHER_ICON_URL.format(it.iconName) }
    var temperature: LiveData<String> = Transformations.map(weatherData) {
        val default = "-"
        "${it.temperature?.toInt() ?: default}"
    }
    var description: LiveData<String> = Transformations.map(weatherData) { cwd ->
        val descriptors: List<String> = cwd.description?.split(" ")
            ?: "request error".split(" ")
        val newDescriptors = mutableListOf<String>()
        descriptors.forEach { desc ->
            newDescriptors.add(
                desc.first().uppercase().plus(
                    desc.slice(1 until desc.length).lowercase())
            )
        }
        newDescriptors.joinToString(" ")
    }
    val location: LiveData<String> = Transformations.map(currentLocation.state) { _ ->
        val identifiers: MutableList<String> = mutableListOf()
        arrayOf(currentLocation.city.value, currentLocation.state.value).forEach {
            if (it != null) {
                identifiers.add(it)
            } else {
                identifiers.add("-")
            }
        }
        identifiers.joinToString(", ")
    }

    init {
        _model.value = this@WeatherCardViewModel
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            _status.value = CurrentWeatherApiStatus.LOADING
            try {
                weatherData.value = CurrentWeatherApi.retrofitService
                    .getCurrentWeatherData(latitude = currentLocation.latitude.value ?: 404.0,
                                           longitude = currentLocation.longitude.value ?: 404.0)
                _status.value = CurrentWeatherApiStatus.DONE
            } catch (e: Exception) {
                Log.d(logTag, "Exception Caught in WeatherCardViewModel: $e")
                _status.value = CurrentWeatherApiStatus.ERROR
                weatherData.value = CurrentWeatherData(null, null)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        currentLocation.update(location)
        fetchWeatherData()
    }

    override fun onProviderDisabled(provider: String) {
        Log.d(logTag, "provider disabled")
    }

    override fun onProviderEnabled(provider: String) {
        currentLocation.update()
        fetchWeatherData()
        Log.d(logTag, "provider enabled")
    }
}