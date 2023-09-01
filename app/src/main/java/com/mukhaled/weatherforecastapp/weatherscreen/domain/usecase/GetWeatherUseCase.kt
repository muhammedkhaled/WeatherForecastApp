package com.mukhaled.weatherforecastapp.weatherscreen.domain.usecase

import com.mukhaled.weatherforecastapp.core.domain.repositories.HomeRepositoryInterface
import com.mukhaled.weatherforecastapp.weatherscreen.domain.model.WeatherUIData
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val homeRepositoryInterface: HomeRepositoryInterface) {
    suspend operator fun invoke(lat: String, lng: String): WeatherUIData {
        return homeRepositoryInterface.getWeather(lat, lng)
    }
}