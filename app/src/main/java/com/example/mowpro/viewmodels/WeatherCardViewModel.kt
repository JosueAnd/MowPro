package com.example.mowpro.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.mowpro.network.CurrentWeatherApi
import com.example.mowpro.network.CurrentWeatherApiStatus
import kotlinx.coroutines.launch
import com.example.mowpro.network.CurrentWeatherData
import com.example.mowpro.network.fakeWeatherData
import java.lang.IllegalArgumentException

class WeatherCardViewModel: ViewModel() {

    private val logTag = "WeatherCardVM"

    private val _currentWeatherData = MutableLiveData<CurrentWeatherData>()
    val currentWeatherData: LiveData<CurrentWeatherData> = _currentWeatherData
    private val _weatherTemp = MutableLiveData<String>()
    val weatherTemp: LiveData<String> = _weatherTemp

    private val _status = MutableLiveData<CurrentWeatherApiStatus>()
//    val status: LiveData<CurrentWeatherApiStatus> = _status

    init { getTheCurrentWeatherData() }

    private fun getTheCurrentWeatherData() {
        viewModelScope.launch {
            _status.value = CurrentWeatherApiStatus.LOADING
            try {
                _currentWeatherData.value = CurrentWeatherApi.retrofitService
                    .getCurrentWeatherData(latitude = "25",
                                           longitude = "25")
                _status.value = CurrentWeatherApiStatus.DONE
            } catch (e: Exception) {
                Log.d(logTag, "Exception Caught: $e")
                _status.value = CurrentWeatherApiStatus.ERROR
                _currentWeatherData.value = fakeWeatherData
            }
            Log.d(logTag, "viewModelScope.launch reached end")
            Log.d(logTag, currentWeatherData.toString())
        }
        Log.d(logTag, "Exited viewModelScope.launch")
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