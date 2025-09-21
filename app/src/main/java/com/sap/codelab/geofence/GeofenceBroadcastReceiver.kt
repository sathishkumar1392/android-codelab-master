package com.sap.codelab.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.sap.codelab.utils.extensions.NotificationHelper

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("DestinationReceiver", "Broadcast received!")
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent?.hasError() == true) {
            Log.e("GeofenceReceiver", "Error: ${geofencingEvent.errorCode}")
            return
        }
        geofencingEvent?.triggeringGeofences?.forEach {
            Log.d("GeofenceReceiver", "Triggered geofence ID: ${it.requestId}")
        }
        when (geofencingEvent?.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Log.d("GeofenceReceiver", "Entered geofence")
                // trigger notification
                NotificationHelper.showNotification(context, "Geofence Alert", "You have entered a geofenced area.")
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.d("GeofenceReceiver", "Exited geofence")
                NotificationHelper.showNotification(context, "Geofence Alert", "You have exited a geofenced area.")
            }
        }
    }
}

