package com.example.weatherapp.Service

import com.example.weatherapp.WeatherNextDays
import com.example.weatherapp.model.HourDailyModel
import com.example.weatherapp.model.NextDaysModel
import com.example.weatherapp.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") location: String,
        @Query("appid") apiKey: String,
    ): WeatherModel

    @GET("forecast")
    suspend fun getHourDaily(
        @Query("q") location: String,
        @Query("appid") apiKey: String
    ): HourDailyModel

//    @GET("forecast/daily") PAGAR
//    suspend fun getNextDays(
//        @Query("q") location: String,
//        @Query("cnt") cnt: String,
//        @Query("appid") apiKey: String
//    ): NextDaysModel

    @GET("widgets/weather")
    suspend fun getNextDays(
        @Query("id") location: String? = "3128760",
        @Query("units") metrics: String? = "metrics",
        @Query("appid") apiKey: String,

    ): WeatherNextDays

}