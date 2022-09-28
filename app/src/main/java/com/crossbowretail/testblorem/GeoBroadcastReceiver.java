package com.crossbowretail.testblorem;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

public class GeoBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = GeoBroadcastReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "Unable to start Geofencing Event");
            return;
        }

        int requestcode = new Random().nextInt();

        int transitiontype = geofencingEvent.getGeofenceTransition();
        switch (transitiontype) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                notificationHelper.sendNotification("Blorem Reminder", "Hi, You've reached your destination", MainActivity.class, requestcode);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                //do something
                break;
        }
    }

}