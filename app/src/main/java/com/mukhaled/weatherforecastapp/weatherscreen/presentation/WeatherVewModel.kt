package com.mukhaled.weatherforecastapp.weatherscreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukhaled.weatherforecastapp.core.utils.createExceptionHandler
import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import com.mukhaled.weatherforecastapp.core.domain.model.Event
import com.mukhaled.weatherforecastapp.core.domain.model.NetworkException
import com.mukhaled.weatherforecastapp.core.domain.model.NetworkUnavailableException
import com.mukhaled.weatherforecastapp.core.presentation.DialogState
import com.mukhaled.weatherforecastapp.core.utils.Logger
import com.mukhaled.weatherforecastapp.weatherscreen.domain.usecase.GetImgs
import com.mukhaled.weatherforecastapp.weatherscreen.domain.usecase.GetWeatherUseCase
import com.mukhaled.weatherforecastapp.weatherscreen.domain.usecase.StoreImg
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WeatherVewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val storeImg: StoreImg,
    private val getImgs: GetImgs,
    private val compositeDisposable: CompositeDisposable,
) : ViewModel() {

    // uiState
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private lateinit var _lat: String
    private lateinit var _lng: String
    private fun setLatLong(lat: String, lng: String) {
        _lat = lat
        _lng = lng
    }

    init {
        subscribeToUpdates()
    }

    // onEvent
    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.GetData -> {
                getInitialData()
            }

            UiEvent.HideSettingsDialog -> {
                _state.update { oldState ->
                    oldState.copy(gpsDialogState = DialogState.Hide)
                }
            }

            UiEvent.ShowSettingsDialog -> {
                _state.update { oldState ->
                    oldState.copy(gpsDialogState = DialogState.Show)
                }
            }

            UiEvent.GetImages -> { }
            is UiEvent.StoreImg -> {
                storeImage(event.path)
            }

            is UiEvent.SetLatLong -> {
                setLatLong(event.lat, event.lng)
            }
        }
    }


    private fun getInitialData() {
        _state.update { oldState ->
            oldState.copy(isLoading = true)
        }
        val errorMessage = "Failed to load Weather data"
        val exceptionHandler =
            viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }
        viewModelScope.launch(exceptionHandler) {
            val weather = getWeatherUseCase(_lat, _lng)
            _state.update { oldState ->
                oldState.copy(isLoading = false, weatherUIData = weather)
            }
        }
    }

    private fun storeImage(path: String){
        viewModelScope.launch {
            storeImg(path)
        }
    }

    private fun subscribeToUpdates() {
        getImgs()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onNewList(it) },
                { onFailure(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun onNewList(list: List<CashedItemData>) {
        Logger.d("Got images")

        _state.update { oldState ->
            oldState.copy(
                isLoading = false,
                imgList = list
            )
        }
    }

    private fun onFailure(failure: Throwable) {
        when (failure) {
            is NetworkException -> _state.update { oldState ->
                oldState.copy(
                    isLoading = false,
                    failure = Event(failure)
                )
            }
            is NetworkUnavailableException -> _state.update { oldState ->
                oldState.copy(
                    isLoading = false,
                    failure = Event(failure)
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // to not cause any memory leak
        compositeDisposable.clear()
    }
}