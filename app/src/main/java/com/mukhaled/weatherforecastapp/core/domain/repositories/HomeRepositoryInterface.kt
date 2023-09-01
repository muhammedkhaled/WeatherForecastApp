package com.mukhaled.weatherforecastapp.core.domain.repositories

import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import com.mukhaled.weatherforecastapp.weatherscreen.domain.model.WeatherUIData
import io.reactivex.Flowable


interface HomeRepositoryInterface {
    fun getAllImages(): Flowable<List<CashedItemData>>
    suspend fun storeImg(item: CashedItemData)
    suspend fun getWeather(lat: String, lng: String): WeatherUIData
}