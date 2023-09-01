package com.mukhaled.weatherforecastapp.weatherscreen.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class WeatherUIData(
    val timeZone: String,
    val date: String,
    val temp: Double,
    val tempInC: String,
    val tempInF: String,
    val humidity: Int,
    val wind_speed: Double,
    val conditionValue: String,
    @DrawableRes val icon: Int
)
