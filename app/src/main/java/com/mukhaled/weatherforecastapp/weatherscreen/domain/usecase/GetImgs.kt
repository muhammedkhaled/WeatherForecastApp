package com.mukhaled.weatherforecastapp.weatherscreen.domain.usecase

import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import com.mukhaled.weatherforecastapp.core.domain.repositories.HomeRepositoryInterface
import com.mukhaled.weatherforecastapp.core.utils.DispatchersProvider
import com.mukhaled.weatherforecastapp.weatherscreen.domain.model.WeatherUIData
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetImgs @Inject constructor(
    private val homeRepositoryInterface: HomeRepositoryInterface,
) {
    operator fun invoke() = homeRepositoryInterface.getAllImages()
}