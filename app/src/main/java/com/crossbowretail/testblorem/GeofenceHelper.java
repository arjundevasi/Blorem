package com.crossbowretail.testblorem;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceHelper extends ContextWrapper {

    private PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeofencingrequest(Geofence geofence, int transitiontype){
        return  new GeofencingRequest.Builder().addGeofence(geofence)
                .setInitialTrigger(transitiontype)
                .build();
    }

    public Geofence  getGeofence (String ID, LatLng latLng, float radius, int transitiontype){
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude,latLng.longitude,radius)
                .setRequestId(ID)
                .setTransitionTypes(transitiontype)
                .setExpirationDuration(Geofence.NEVER_EXPIRE).build();
    }
    public PendingIntent getPendingIntent(){
        if (pendingIntent!=null){
            return pendingIntent;
        }
        Intent intent = new Intent(this,GeoBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,76,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
