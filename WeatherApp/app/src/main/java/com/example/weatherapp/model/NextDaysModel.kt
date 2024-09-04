package com.example.weatherapp.model

data class NextDaysModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DailyInfo>,
    val message: Double
)