package com.mukhaled.weatherforecastapp.core.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiWeather(
    val current: Current,
    val lat: String,
    val lon: String,
    val timezone: String,
)

@JsonClass(generateAdapter = true)
data class Current(
    val dt: Int,
    val clouds: Int,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind_speed: Double,
)

@JsonClass(generateAdapter = true)
data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String,
)
