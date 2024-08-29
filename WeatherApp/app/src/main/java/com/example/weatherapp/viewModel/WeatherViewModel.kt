package com.example.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WeatherState{
    object Loading:WeatherState()
    data class Success(val weather: WeatherModel):WeatherState()
    data class Error(val message:String):WeatherState()
}

class WeatherViewModel : ViewModel(){
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    fun fetchWeatherData(city:String){
        viewModelScope.launch {
            try{
                val response = WeatherApi.weatherService.getWeather(
                    apiKey = "544051addec007c3e927d2d16b40c737",
                    location = city
                )

                _weatherState.value = WeatherState.Success(response)
            }catch (e: Exception){
                _weatherState.value = WeatherState.Error(e.message ?: "Unknown error")

            }
        }
    }
}