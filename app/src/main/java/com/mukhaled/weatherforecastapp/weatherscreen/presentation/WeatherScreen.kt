package com.mukhaled.weatherforecastapp.weatherscreen.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mukhaled.weatherforecastapp.core.presentation.ComposableLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mukhaled.weatherforecastapp.core.utils.findActivity
import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import com.mukhaled.weatherforecastapp.core.presentation.DialogState
import com.mukhaled.weatherforecastapp.core.utils.turnGpsOn
import com.mukhaled.weatherforecastapp.weatherscreen.presentation.componnets.PermissionDialog
import com.mukhaled.weatherforecastapp.weatherscreen.presentation.componnets.WeatherCard
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(state: UiState, onEvent: (UiEvent) -> Unit) {
    val applicationContext = LocalContext.current.applicationContext
    val activity = LocalContext.current.findActivity()

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val locationPermissionState = rememberSaveable(state.gpsDialogState) { mutableStateOf(false) }
    if (locationPermissionState.value) onEvent(UiEvent.ShowSettingsDialog)

    val getLatLngClick: () -> Unit = {
        val gpsState =
            (applicationContext.getSystemService(ComponentActivity.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
        if (!gpsState) {
            Toast.makeText(activity, "Open Gps", Toast.LENGTH_LONG).show()
            turnGpsOn(activity)
        } else {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(applicationContext)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    onEvent(
                        UiEvent.SetLatLong(
                            location.latitude.toString(), location.longitude.toString()
                        )
                    )
                    onEvent(UiEvent.GetData)
                }
            }
        }
    }

    if (!locationPermissionsState.allPermissionsGranted) {
        val allPermissionsRevoked =
            locationPermissionsState.permissions.size == locationPermissionsState.revokedPermissions.size
        var textToShow = ""
        if (!allPermissionsRevoked) {
            // If not all the permissions are revoked, it's because the user accepted the COARSE
            // location permission, but not the FINE one.
            textToShow =
                "Yay! Thanks for letting me access your approximate location. " + "But you know what would be great? If you allow me to know where you " + "exactly are. Thank you!"
        } else if (locationPermissionsState.shouldShowRationale) {
            // Both location permissions have been denied
            textToShow =
                "Getting your exact location is important for this app. " + "Please grant us fine location. Thank you :D"
        } else {
            // First time the user sees this feature or the user doesn't want to be asked again
            textToShow = "This feature requires location permission"
            onEvent(UiEvent.ShowSettingsDialog)
        }
        Toast.makeText(applicationContext, textToShow, Toast.LENGTH_LONG).show()
    }

    ComposableLifecycle { source, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            // request location permision
            locationPermissionsState.launchMultiplePermissionRequest()
            locationPermissionState.value = !locationPermissionsState.allPermissionsGranted
            getLatLngClick.invoke()
        }
    }

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    state.failure?.getContentIfNotHandled()?.let { throwable ->
        val connectionError = "No Internet"
        coroutineScope.launch {
            val snackBarResult = snackBarHostState.showSnackbar(
                message = throwable.message ?: connectionError, actionLabel = "Try Again"
            )
            when (snackBarResult) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {
                    getLatLngClick.invoke()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center), color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Time Zone: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = state.weatherUIData.timeZone,
                    fontWeight = FontWeight.Light,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 3.dp)
                )
            }
            WeatherCard(state = state, onEvent = onEvent)
            ImagesList(state.imgList)
        }
        SnackbarHost(modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackBarHostState,
            snackbar = { Snackbar(snackbarData = it) })
    }


    when (state.gpsDialogState) {
        DialogState.Show -> {
            PermissionDialog(onDismiss = {
                if (locationPermissionsState.allPermissionsGranted) {
                    onEvent(UiEvent.HideSettingsDialog)
                    locationPermissionState.value = false
                }
            }, openSettings = {
                applicationContext.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", applicationContext.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            })
        }

        DialogState.Hide -> {}
    }
}


@Composable
fun ImagesList(list: List<CashedItemData>) {
    Text(
        text = "History Of Captured Images", fontSize = 18.sp, modifier = Modifier.padding(8.dp)
    )
    LazyRow() {
        items(list.size) { itemIndex ->
            val item = list[itemIndex]
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(item.imgPath).build(),
                    contentDescription = "icon",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.size(240.dp)
                )
                Text(text = item.date, fontSize = 12.sp)
            }
        }
    }
}


@Preview
@Composable
fun WeatherScreenPrev() {
    WeatherScreen(state = UiState(), onEvent = {})
}