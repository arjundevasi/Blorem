package com.crossbowretail.testblorem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.RadioAccessSpecifier;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.util.Random;

public class GeoFenceAddActivity extends AppCompatActivity implements OnMapReadyCallback {

    private double latitude = 0;
    private double longitude = 0;
    private GoogleMap map;
    private LatLng latLng;
    private int radius = 1000;
    private String geofence_id;
    private GeofencingClient geofencingClient;
    private NotificationHelper notificationHelper;
    private GeofenceHelper geofenceHelper;
    private String placename;
    private String placeDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence_add);

        //textviews for place name and details
        TextView nameTextview = (TextView) findViewById(R.id.name_text);
        TextView detailTextview = (TextView) findViewById(R.id.details_text);

        Button save_button  = (Button)findViewById(R.id.add_geofence);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    saveGeofence();
            }
        });

        Intent intent = getIntent();
        if (intent != null){
            latitude = intent.getDoubleExtra("lat",0);
            longitude = intent.getDoubleExtra("lng",0);
            placename = intent.getStringExtra("name");
            placeDetails = intent.getStringExtra("details");
            if (placename != null && placeDetails != null){
                nameTextview.setText(placename);
                detailTextview.setText(placeDetails);
            }else {
                nameTextview.setVisibility(View.GONE);
                detailTextview.setVisibility(View.GONE);
            }
        }

        latLng = new LatLng(latitude,longitude);


        // initialize seekbar
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               seekbar(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //adding geofence id
        geofence_id = "com.crossbowretail.testblorem" + new Random().nextInt();

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);

        notificationHelper = new NotificationHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_geofence_activity);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(false);
        setFixedmap(latLng);
        setCircle(latLng);
    }
    private void setFixedmap(LatLng latLng){
        map.addMarker(new MarkerOptions().position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }
    private void setCircle(LatLng latLng){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.strokeColor(Color.BLACK).strokeWidth(2).fillColor(Color.argb(128,12,125,255));
        circleOptions.radius(radius).center(latLng);
        map.addCircle(circleOptions);
    }
    private void seekbar(int progress){
        map.clear();
        radius = progress *100;
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng).radius(radius);
        circleOptions.strokeColor(Color.BLACK).fillColor(Color.argb(128,12,125,255)).strokeWidth(2);
        map.addCircle(circleOptions);
    }

    @SuppressLint("MissingPermission")
    private void saveGeofence(){
            LatLng geo_latlng = latLng;
            Geofence geofence = geofenceHelper.getGeofence(geofence_id, geo_latlng, radius, Geofence.GEOFENCE_TRANSITION_ENTER);
            GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingrequest(geofence,Geofence.GEOFENCE_TRANSITION_ENTER);
            PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(GeoFenceAddActivity.this, "Reminder added", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GeoFenceAddActivity.this, "Failed to set Geofence", Toast.LENGTH_SHORT).show();
                        }
                    });
            gotoMainactivity();



    }
    private void gotoMainactivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}