package com.mukhaled.weatherforecastapp.weatherscreen.presentation

sealed class UiEvent {
    object GetData : UiEvent()
    data class SetLatLong(val lat: String, val lng: String) : UiEvent()
    object ShowSettingsDialog : UiEvent()
    object HideSettingsDialog : UiEvent()
    data class StoreImg(val path: String) : UiEvent()
    object GetImages : UiEvent()
}