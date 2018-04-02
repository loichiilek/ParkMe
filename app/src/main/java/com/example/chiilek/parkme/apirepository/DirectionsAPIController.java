package com.example.chiilek.parkme.apirepository;

import android.util.Log;

import com.example.chiilek.parkme.data_classes.directions_classes.GoogleMapsDirections;
import com.example.chiilek.parkme.data_classes.directions_classes.Step;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectionsAPIController {

    static final private String GMAPS_DIRECTION_API_BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    static final private String GMAPS_API_KEY = "AIzaSyB0R4S6MUMQ_tuEfT29tr4RQnZmcGqo1Qo";

    public void callDirectionsAPI(LatLng origin, LatLng destination){
        Map<String, String> params = new HashMap<String,String>();
        params.put("origin", "75 9th Ave New York, NY");
        params.put("destination", "MetLife Stadium 1 MetLife Stadium Dr East Rutherford, NJ 07073");
        params.put("mode", "driving");
        params.put("key", GMAPS_API_KEY);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GMAPS_DIRECTION_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GMapsDirectionsAPI directionsAPI = retrofit.create(GMapsDirectionsAPI.class);

        Call<GoogleMapsDirections> call = directionsAPI.getDirections(params);
        System.out.println("before enqueue");

        call.enqueue(new Callback<GoogleMapsDirections>(){
            @Override
            public void onResponse(Call<GoogleMapsDirections> call, Response<GoogleMapsDirections> response){
                Log.d("API Controller  (G Maps)","in response");
                GoogleMapsDirections gMapsDirections = response.body();
//              String overviewPolyline = gMapsDirections.getRoutes().get(0).getOverviewPolyline().getPoints();
                List<Step> steps = gMapsDirections.getRoutes().get(0).getLegs().get(0).getSteps();
                //TODO: get library to decode string polyline into latlng (?) or use latlng to plot
            }

            @Override
            public void onFailure(Call<GoogleMapsDirections> call, Throwable t){
                t.printStackTrace();
            }
        });

        //for testing the api call
//        try {
//            Response<GoogleMapsDirections> response = call.execute();
//            System.out.println("in response");
//            GoogleMapsDirections gMapsDirections = response.body();
//            Log.d("test", "called .body method");
//            Log.d("test", call.request().url().toString());
//            Log.d("test", "response code: " + gMapsDirections.getStatus());
//            Log.d("test", gMapsDirections.getRoutes().get(0).
//                    getOverviewPolyline().getPoints());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
