package com.mukhaled.weatherforecastapp.core.data.api

import com.mukhaled.weatherforecastapp.BuildConfig
import com.mukhaled.weatherforecastapp.core.data.api.model.ApiWeather
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GetWeatherApi {

    @Headers("${ApiParameters.ACCEPT}: application/json")
    @GET("/data/2.5/onecall?")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("appid") appId: String = BuildConfig.AUTH_KEY,
        @Query("exclude") exclude: String = "minutely,alerts"
    ): ApiWeather
}