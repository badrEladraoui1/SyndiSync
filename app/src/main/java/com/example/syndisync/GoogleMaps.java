package com.example.syndisync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.util.Log;

import org.checkerframework.common.returnsreceiver.qual.This;

public class GoogleMaps extends AppCompatActivity implements LocationSource.OnLocationChangedListener, OnMapReadyCallback, LocationListener {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap map;
    MapView mapview;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        // get map view
        mapview = findViewById(R.id.mapView);
        mapview.onCreate(savedInstanceState);
        mapview.getMapAsync(this);

        // Initialize LocationManager
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        map.clear();
// Add a marker for the current location
        LatLng currentLocation = new LatLng(location.getLatitude(),
                location.getLongitude());
        Log.i("test", currentLocation.toString());
        map.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
// Move camera to the current location
//        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//        map.animateCamera(CameraUpdateFactory.zoomTo(13));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        this.map = googleMap; // Set the map variable

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
// Request runtime permissions if not granted
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L, (float) 0, this);
        Log.i("listner", "testing listner");
    }
}