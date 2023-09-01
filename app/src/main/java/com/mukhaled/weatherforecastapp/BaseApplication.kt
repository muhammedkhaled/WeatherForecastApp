package com.mukhaled.weatherforecastapp

import android.app.Application
import com.mukhaled.weatherforecastapp.core.utils.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

  override fun onCreate() {
    super.onCreate()
    initLogger()
  }

  private fun initLogger() {
    Logger.init()
  }
}
