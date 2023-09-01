package com.mukhaled.weatherforecastapp.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukhaled.weatherforecastapp.core.presentation.ui.theme.WeatherForecastAppTheme
import com.mukhaled.weatherforecastapp.weatherscreen.presentation.WeatherScreen
import com.mukhaled.weatherforecastapp.weatherscreen.presentation.WeatherVewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherForecastAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<WeatherVewModel>()
                    WeatherScreen(viewModel.state.collectAsState().value) {
                        viewModel.onEvent(it)
                    }
                }
            }
        }
    }
}

