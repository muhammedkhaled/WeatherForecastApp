package com.mukhaled.weatherforecastapp.core.data.cash

import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import io.reactivex.Flowable


interface Cache {
  suspend fun storeWeatherImg(item: CashedItemData)
  fun getAll(): Flowable<List<CashedItemData>>
}
