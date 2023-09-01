package com.mukhaled.weatherforecastapp.weatherscreen.presentation

import com.mukhaled.weatherforecastapp.R
import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import com.mukhaled.weatherforecastapp.core.domain.model.Event
import com.mukhaled.weatherforecastapp.core.presentation.DialogState
import com.mukhaled.weatherforecastapp.weatherscreen.domain.model.WeatherUIData

data class UiState(
    val isLoading: Boolean = false,
    val gpsDialogState: DialogState = DialogState.Hide,
    val weatherUIData: WeatherUIData = WeatherUIData(
        "",
        "",
        Double.NaN,
        "",
        "",
        0,
        Double.NaN,
        "",
        R.drawable.ic_sun
    ),
    val imgList: List<CashedItemData> = emptyList(),
    val failure: Event<Throwable>? = null,
)