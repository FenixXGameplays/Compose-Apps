package com.example.weatherapp.model

data class HourDailyModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<HourInfo>,
    val message: Int
)