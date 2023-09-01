package com.mukhaled.weatherforecastapp.core.data

import com.mukhaled.weatherforecastapp.core.data.api.GetWeatherApi
import com.mukhaled.weatherforecastapp.core.data.api.model.mappers.WeatherMapper
import com.mukhaled.weatherforecastapp.core.data.cash.Cache
import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import com.mukhaled.weatherforecastapp.core.domain.model.NetworkException
import com.mukhaled.weatherforecastapp.core.domain.repositories.HomeRepositoryInterface
import com.mukhaled.weatherforecastapp.weatherscreen.domain.model.WeatherUIData
import io.reactivex.Flowable
import retrofit2.HttpException
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val getWeatherApi: GetWeatherApi,
    private val mapper: WeatherMapper,
    private val cache: Cache,
) : HomeRepositoryInterface {

    override fun getAllImages(): Flowable<List<CashedItemData>> =
        cache.getAll().distinctUntilChanged()

    override suspend fun storeImg(item: CashedItemData) = cache.storeWeatherImg(item)

    override suspend fun getWeather(lat: String, lng: String): WeatherUIData {
        try {
            return mapper.mapToDomain(getWeatherApi.getWeather(lat = lat, lng = lng))
        } catch (e: HttpException) {
            throw handleException(e)
        }
    }

    private fun handleException(exception: HttpException): Exception =
    // TODO: get error schema and parse error
        // val errorResponse = convertErrorBody(exception)
        throw NetworkException(
            message = exception.message ?: "Code ${exception.code()}", code = exception.code()
        )
}


//    private fun convertErrorBody(exception: HttpException): ApiMessage? {
//        return try {
//            exception.response()?.errorBody()?.source()?.let {
//                val moshiAdapter = Moshi.Builder().build().adapter(ApiMessage::class.java)
//                moshiAdapter.fromJson(it)
//            }
//        } catch (exception: Exception) {
//            null
//        }
//    }

