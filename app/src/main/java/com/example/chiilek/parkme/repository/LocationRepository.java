package com.example.chiilek.parkme.repository;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationRepository  {

    private FusedLocationProviderClient mFusedClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private MutableLiveData<LatLng> currentLocation;
    private static LocationRepository INSTANCE;
    private Context mContext;

    private LocationRepository(Context context){
        mContext = context;
        mFusedClient = LocationServices.getFusedLocationProviderClient(mContext);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {return;}
                Location location = locationResult.getLastLocation();
                currentLocation.setValue(new LatLng(location.getLatitude(), location.getLongitude()));
                Log.d("LocationRepo","Location Update is " + location.getLatitude() + " "
                        + location.getLongitude());
            }
        };
        startLocationUpdate(mLocationCallback);
        currentLocation = new MutableLiveData<>();
        //TODO find out how to not hardcode this value without getting a null pointer
        currentLocation.setValue(new LatLng(1.353675,103.687791));

        Log.d("LocationRepo", "LocationRepository constructed");
    }

    public static LocationRepository getLocationRepository(Context context){
        if (INSTANCE == null)
            INSTANCE = new LocationRepository(context.getApplicationContext());
        Log.d("LocationRepo", "called get singleton");
        return INSTANCE;
    }

    private void startLocationUpdate(LocationCallback mLocationCallback){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient client = LocationServices.getSettingsClient(mContext);
        client.checkLocationSettings(locationSettingsRequest);

        //check for permission before you can request updates
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            mFusedClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            Log.d("LocationRepo","requested updates");
        } else {
            //do not need because we are requesting for permission in UI
            Log.d("LocationRepo","update permission not granted");
        }
    }
    public MutableLiveData<LatLng> getLocation(){
        Log.d("LocationRepo", "getLocation called, returning current location " + currentLocation.getValue().toString());
        return this.currentLocation;
    }

    public MutableLiveData<LatLng> getLastLocation(){
        Log.d("Location Repo", "Getting Last Location");
        if (mFusedClient == null)
            mFusedClient = LocationServices.getFusedLocationProviderClient(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mFusedClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                Log.d("LocationRepo","getLastLocation is " + location.getLongitude() + location.getLatitude());
                                currentLocation.setValue(new LatLng(location.getLatitude(),location.getLongitude()));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("LocationRepo","Failed to get last location");
                            e.printStackTrace();
                        }
                    });
        }
        return this.currentLocation;
    }

    public void stopLocationUpdates(){
        Log.d("LocationRepo", "stopping location updates");
        mFusedClient.removeLocationUpdates(mLocationCallback);
    }
}