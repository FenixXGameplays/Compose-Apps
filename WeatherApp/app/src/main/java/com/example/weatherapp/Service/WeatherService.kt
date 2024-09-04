package com.example.weatherapp.Service

import com.example.weatherapp.model.HourDailyModel
import com.example.weatherapp.model.NextDaysModel
import com.example.weatherapp.model.WeatherModel
import retrofit2.http.GET
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

    @GET("forecast/daily")
    suspend fun getNextDays(
        @Query("q") location: String,
        @Query("cnt") quantity: String,
        @Query("appid") apiKey: String
    ): NextDaysModel

}