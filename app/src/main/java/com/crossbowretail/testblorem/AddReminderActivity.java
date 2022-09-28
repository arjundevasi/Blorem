package com.crossbowretail.testblorem;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.crossbowretail.testblorem.databinding.ActivityAddReminderBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class AddReminderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityAddReminderBinding binding;
    private Context context;
    private Marker marker;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String place_name;
    private String place_details;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getting a variable context
        context = getApplicationContext();

        //initializing the places
        final String api_key = BuildConfig.MAPS_API_KEY;
        Places.initialize(this, api_key);
        PlacesClient placesClient = Places.createClient(this);

        //get last location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        //get last known location and move camera there
        lastknownlocation();

        //changing the position of my location button
        mylocationbuttonpos();

        //setup autocomplete place search
        autoCompletePlacesetup();

        // on clicking on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mapClick(latLng);
            }
        });

    }

    // this method changes the position of my location button
    private void mylocationbuttonpos(){
        View my_locationbutton = ((View)findViewById(Integer.parseInt("1")).getParent())
                .findViewById(Integer.parseInt("2"));

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                my_locationbutton.getLayoutParams();

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        layoutParams.setMargins(0,150,40,0);
    }

    private void autoCompletePlacesetup(){

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.autoCompletefrsgment);

        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG));


        autocompleteSupportFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteSupportFragment.setTypeFilter(TypeFilter.REGIONS);

        // on places selected
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(AddReminderActivity.this, "Unable to search", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                place_name = place.getName();
                place_details = place.getAddress();

                mMap.clear();

                // if we have a plcae then position the camera
                if (latLng != null){
                    marker = mMap.addMarker(new MarkerOptions().position(latLng));
                    openGeoAddActivityonPlaceclick(latLng,place_name,place_details);
                }


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });
    }

    private void mapClick(LatLng latLng){

        mMap.clear();

        marker = mMap.addMarker(new MarkerOptions().position(latLng));

        marker.setTag(latLng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));



        //open geo add activity
        openGeoAddActivityonMapClick(latLng);

    }
    private void openGeoAddActivityonMapClick(LatLng latLng){
        double lat = latLng.latitude;
        double lng = latLng.longitude;
        Intent i = new Intent(AddReminderActivity.this,GeoFenceAddActivity.class);
        i.putExtra("lat",lat);
        i.putExtra("lng",lng);
        startActivity(i);
    }

    private void openGeoAddActivityonPlaceclick(LatLng latLng,String name, String details){
        double lat = latLng.latitude;
        double lng = latLng.longitude;
        Intent i = new Intent(AddReminderActivity.this,GeoFenceAddActivity.class);
        i.putExtra("lat",lat);
        i.putExtra("lng",lng);
        i.putExtra("name",name);
        i.putExtra("details",details);
        startActivity(i);
    }

    @SuppressLint("MissingPermission")
    private void lastknownlocation(){
       fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                }
            }
        });
    }
}