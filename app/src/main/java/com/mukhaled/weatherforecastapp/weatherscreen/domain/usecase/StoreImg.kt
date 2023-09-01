package com.mukhaled.weatherforecastapp.weatherscreen.domain.usecase

import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import com.mukhaled.weatherforecastapp.core.domain.repositories.HomeRepositoryInterface
import com.mukhaled.weatherforecastapp.core.utils.DispatchersProvider
import com.mukhaled.weatherforecastapp.weatherscreen.domain.model.WeatherUIData
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class StoreImg @Inject constructor(
    private val homeRepositoryInterface: HomeRepositoryInterface,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(path: String) {
        withContext(dispatchersProvider.io()){
            val date = SimpleDateFormat("yyyy_MM_dd_HHmmss").format(Date())
            homeRepositoryInterface.storeImg(CashedItemData(date = date, imgPath = path))
        }
    }
}