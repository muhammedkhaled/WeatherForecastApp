package com.mukhaled.weatherforecastapp.weatherscreen.presentation.componnets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mukhaled.weatherforecastapp.R

@Composable
fun PermissionDialog(onDismiss: () -> Unit, openSettings: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Location permission",)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_place),
                    contentDescription = null
                )
            }
        },
        text = {
            Text(
                text = "Weather info is based on you location",
                textAlign = TextAlign.Start,
            )
        },
        confirmButton = {
            TextButton(onClick = openSettings) {
                Text(text = "Open settings",)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Close", color = Color.Gray)
            }
        }
    )
}