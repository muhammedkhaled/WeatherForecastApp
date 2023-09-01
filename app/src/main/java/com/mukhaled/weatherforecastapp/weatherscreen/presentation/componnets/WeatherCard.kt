package com.mukhaled.weatherforecastapp.weatherscreen.presentation.componnets

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.mukhaled.weatherforecastapp.core.utils.createImageFile
import com.mukhaled.weatherforecastapp.BuildConfig
import com.mukhaled.weatherforecastapp.R
import com.mukhaled.weatherforecastapp.core.data.api.ApiConstants
import com.mukhaled.weatherforecastapp.weatherscreen.presentation.UiEvent
import com.mukhaled.weatherforecastapp.weatherscreen.presentation.UiState
import java.util.Objects

@Preview
@Composable
fun WeatherCardPrev() {
    WeatherCard(state = UiState(), onEvent = {})
}

@Composable
fun WeatherCard(modifier: Modifier = Modifier, state: UiState, onEvent: (UiEvent) -> Unit) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )
    // todo put inside viewmodel for oriantation change
    var capturedImageUri by rememberSaveable {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        capturedImageUri = uri
        val path = file.path
        onEvent(UiEvent.StoreImg(path = path))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(18.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val columnModifier = if (capturedImageUri.path?.isNotEmpty() == true) {
            Modifier
                .paint(
                    painter = rememberAsyncImagePainter(capturedImageUri),
                    contentScale = ContentScale.FillBounds
                )
                .fillMaxWidth()
        } else {
            Modifier.fillMaxWidth()
        }
        Box(modifier = modifier) {
            IconButton(modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart), onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    // Request a permission
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                    contentDescription = "change back ground"
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = state.weatherUIData.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(70.dp, 70.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = columnModifier
                ) {
                    Text(
                        text = state.weatherUIData.conditionValue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = state.weatherUIData.date,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = state.weatherUIData.tempInC,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 48.sp
                    )
                    Text(
                        text = state.weatherUIData.tempInF,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 48.sp
                    )

                    Spacer(modifier = Modifier.padding(4.dp))
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 8.dp, top = 4.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(color = Color.White)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WeatherDetail("Wind speed", state.weatherUIData.wind_speed.toString())
                        WeatherDetail("Humidity", state.weatherUIData.humidity.toString())
                    }
                }
            }

            if (state.isLoading) CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center), color = Color.White
            )

            if (!state.isLoading && state.weatherUIData.temp.isNaN()) {
                Button(modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        onEvent(UiEvent.SetLatLong(ApiConstants.LATITUDE, ApiConstants.LONGITUDE))
                        onEvent(UiEvent.GetData)
                    }) {
                    Text(text = "Refresh with initial place")
                }
            }
        }
    }
}

@Composable
fun WeatherDetail(title: String, content: String) {
    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontWeight = FontWeight.W300,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(3.dp))
        Text(
            text = content,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}
