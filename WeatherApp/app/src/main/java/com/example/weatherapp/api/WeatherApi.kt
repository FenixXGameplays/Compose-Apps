package com.example.weatherapp.api

import com.example.weatherapp.Service.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApi{
    private val retrofit= Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val weatherService: WeatherService = retrofit.create(WeatherService::class.java)

}