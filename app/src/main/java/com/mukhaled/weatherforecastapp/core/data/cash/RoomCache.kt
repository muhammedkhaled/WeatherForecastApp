package com.mukhaled.weatherforecastapp.core.data.cash

import com.mukhaled.weatherforecastapp.core.data.cash.Cache
import com.mukhaled.weatherforecastapp.core.data.cash.daos.Dao
import javax.inject.Inject

import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import io.reactivex.Flowable

class RoomCache @Inject constructor(
    private val dao: Dao,
) : Cache {
    override suspend fun storeWeatherImg(item: CashedItemData) = dao.insertItem(item)
    override fun getAll(): Flowable<List<CashedItemData>> = dao.getAll()
}

