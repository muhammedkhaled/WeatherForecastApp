package com.mukhaled.weatherforecastapp.core.utils

import android.app.Activity
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

fun turnGpsOn(activity: Activity) {
    val locationRequest: LocationRequest = LocationRequest.create()
    locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY
    locationRequest.interval = 10000

    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val client = LocationServices.getSettingsClient(activity)
    val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
    task.addOnFailureListener(activity) { e ->
        if (e is ResolvableApiException) {
            try {
                val resolvable = e as ResolvableApiException
                resolvable.startResolutionForResult(
                    (activity),
                    23
                )
            } catch (sendEx: IntentSender.SendIntentException) {
                sendEx.printStackTrace()
            }
        }
    }
}