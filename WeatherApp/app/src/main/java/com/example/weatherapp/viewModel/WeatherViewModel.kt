package com.example.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.WeatherNextDays
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.model.HourDailyModel
import com.example.weatherapp.model.NextDaysModel
import com.example.weatherapp.model.WeatherModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WeatherState{
    object Loading:WeatherState()
    data class Success(val weather: WeatherModel):WeatherState()
    data class Error(val message:String):WeatherState()
}

//sealed class WeatherNextDaysState{
//    object Loading:WeatherNextDaysState()
//    data class Success(val weather: NextDaysModel):WeatherNextDaysState()
//    data class Error(val message:String):WeatherNextDaysState()
//}

sealed class WeatherNextDaysState{
    object Loading:WeatherNextDaysState()
    data class Success(val weather: WeatherNextDays):WeatherNextDaysState()
    data class Error(val message:String):WeatherNextDaysState()
}

sealed class WeatherHourInfoState{
    object Loading:WeatherHourInfoState()
    data class Success(val weather: HourDailyModel):WeatherHourInfoState()
    data class Error(val message:String):WeatherHourInfoState()
}

class WeatherViewModel : ViewModel(){
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private val _weatherNextDaysState = MutableStateFlow<WeatherNextDaysState>(WeatherNextDaysState.Loading)
    val weatherNextDaysState: StateFlow<WeatherNextDaysState> = _weatherNextDaysState

    private val _weatherHourInfoState = MutableStateFlow<WeatherHourInfoState>(WeatherHourInfoState.Loading)
    val weatherHourInfoState: StateFlow<WeatherHourInfoState> = _weatherHourInfoState

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

    fun fetchDailyHourWeatherData(city:String){
        viewModelScope.launch {
            try{
                val response = WeatherApi.weatherService.getHourDaily(
                    apiKey = "544051addec007c3e927d2d16b40c737",
                    location = city
                )

                _weatherHourInfoState.value = WeatherHourInfoState.Success(response)
            }catch (e: Exception){
                _weatherHourInfoState.value = WeatherHourInfoState.Error(e.message ?: "Unknown error")

            }
        }
    }

//    fun fetchNextDaysWeatherData(city:String){
//        viewModelScope.launch {
//            try{
//                val response = WeatherApi.weatherService.getNextDays(
//                    apiKey = "544051addec007c3e927d2d16b40c737",
//                    cnt = "7",
//                    location = city
//                )
//
//                _weatherNextDaysState.value = WeatherNextDaysState.Success(response)
//            }catch (e: Exception){
//                _weatherNextDaysState.value = WeatherNextDaysState.Error(e.message ?: "Unknown error")
//
//            }
//        }
//    }


}