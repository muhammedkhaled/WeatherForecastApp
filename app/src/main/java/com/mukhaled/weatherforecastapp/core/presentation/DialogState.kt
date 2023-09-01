package com.mukhaled.weatherforecastapp.core.presentation

sealed class DialogState {
    object Show : DialogState()
    object Hide : DialogState()
}