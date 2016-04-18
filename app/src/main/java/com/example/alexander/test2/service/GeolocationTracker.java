package com.example.alexander.test2.service;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

public class GeolocationTracker implements LocationListener{

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 30; // 30 minut

    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;

    private double latitude;
    private double longitude;

    public GeolocationTracker(Context context){

        if (Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                return;
        }

        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;

        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (isNetworkEnabled || isGPSEnabled)
        {
            if(location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean canConnectToGeolocationService(){
        return isGPSEnabled || isNetworkEnabled;
    }

}
