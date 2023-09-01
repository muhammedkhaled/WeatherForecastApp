package com.mukhaled.weatherforecastapp.core.data.api.model.mappers

import com.mukhaled.weatherforecastapp.R
import com.mukhaled.weatherforecastapp.core.data.api.model.ApiWeather
import com.mukhaled.weatherforecastapp.weatherscreen.domain.model.WeatherUIData
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class WeatherMapper @Inject constructor() : ApiMapper<ApiWeather, WeatherUIData> {
    override fun mapToDomain(apiEntity: ApiWeather): WeatherUIData {
        return WeatherUIData(
            timeZone = apiEntity.timezone,
            temp = apiEntity.current.temp,
            tempInC = parseTemperatureInCelsius(apiEntity.current.temp),
            tempInF = parseTemperatureInFahrenheit(apiEntity.current.temp),
            date = getDateTime(apiEntity.current.dt).orEmpty(),
            humidity = apiEntity.current.humidity,
            wind_speed = apiEntity.current.wind_speed,
            conditionValue = apiEntity.current.weather.first().main,
            icon = parseImageFromUrl(apiEntity.current.weather.first().icon)
        )
    }

    private fun getDateTime(s: Int): String? {
        val sdf = SimpleDateFormat("dd MM yyyy")
        val netDate = Date(s.toLong() * 1000)
        return sdf.format(netDate)
    }

    private fun parseImageFromUrl(url: String): Int {
        return when (url) {
            "01d" -> R.drawable.ic_sun
            "01n" -> R.drawable.ic_moon
            "02d" -> R.drawable.ic_few_clouds
            "02n" -> R.drawable.ic_night_clouds
            "03d", "03n" -> R.drawable.ic_scattered_clouds
            "04d", "04n" -> R.drawable.ic_broken_clouds
            "09d", "09n" -> R.drawable.ic_shower_rain
            "10d", "10n" -> R.drawable.ic_rain
            "11d", "11n" -> R.drawable.ic_thunderstorm
            "13d", "13n" -> R.drawable.ic_snow
            "50d", "50n" -> R.drawable.ic_mist
            else -> R.drawable.ic_sun
        }
    }

    private fun parseTemperatureInCelsius(temp: Double): String {
        val formattedTemp = temp - 273.15
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.UP
        return "${df.format(formattedTemp)}°C"
    }

    private fun parseTemperatureInFahrenheit(temp: Double): String {
        val formattedTemp = temp - 273.15
        val f = ((formattedTemp * 9) / 5) + 32
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.UP
        return "${df.format(f)}°F"
    }


}