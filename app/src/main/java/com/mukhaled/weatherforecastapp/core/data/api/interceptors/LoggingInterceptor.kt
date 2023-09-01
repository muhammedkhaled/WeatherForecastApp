package com.mukhaled.weatherforecastapp.core.data.api.interceptors

import com.mukhaled.weatherforecastapp.core.utils.Logger
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class LoggingInterceptor @Inject constructor() : HttpLoggingInterceptor.Logger {

  override fun log(message: String) {
    Logger.i(message)
  }
}