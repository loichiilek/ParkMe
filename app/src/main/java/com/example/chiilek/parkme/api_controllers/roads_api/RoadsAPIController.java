package com.example.chiilek.parkme.api_controllers.roads_api;

import android.util.Log;

import com.example.chiilek.parkme.data_classes.directions_classes.GoogleMapsDirections;
import com.example.chiilek.parkme.data_classes.directions_classes.Step;
import com.example.chiilek.parkme.data_classes.roads_classes.GMapsRoads;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RoadsAPIController {
    static final private String GMAPS_ROADS_API_BASE_URL = "https://roads.googleapis.com/v1/";
    static final private String GMAPS_API_KEY = "AIzaSyCl6vY67OT8SbBtih9kD1zttnk9lOUxbT4";

    private GMapsRoads gMapsRoads;

    private static RoadsAPIController INSTANCE;

    public static RoadsAPIController getInstance(){
        if (INSTANCE == null)
            INSTANCE = new RoadsAPIController();
        Log.d("RoadsAPIController", "in singleton pattern");
        return INSTANCE;
    }

    public GMapsRoads getRoads(GoogleMapsDirections gMapsDir){
        String pathString = "";
        List<Step> steps = gMapsDir.getRoutes().get(0).getLegs().get(0).getSteps();
        for(Step step : steps){
            pathString = pathString + step.getStartLocation().getLat() + "," + step.getStartLocation().getLng() + "|";
        }
        pathString = pathString.substring(0,pathString.length()-2);
        Log.d("RoadsAPIController", "getRoads: path string: " + pathString);

        Map<String, String> params = new HashMap<String,String>();
        params.put("path", pathString);
        params.put("interpolate", "true");
        params.put("key", GMAPS_API_KEY);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GMAPS_ROADS_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RoadsAPI roadsAPI = retrofit.create(RoadsAPI.class);

        Call<GMapsRoads> call = roadsAPI.getDirections(params);

        AtomicInteger i = new AtomicInteger(1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    try {
                        i.decrementAndGet();
                        Response<GMapsRoads> response = call.execute();
                        Log.d("RoadsAPIController", "call executed");
                        Log.d("RoadsAPIController", "Status code: " + response.code());
                        Log.d("RoadsAPIController", "URL: " + response.raw().request().url());
                        setRoads(response.body());
                    } catch (IOException e) {
                        i.decrementAndGet();
                        e.printStackTrace();
                        Log.d("RoadsAPIController", "response is null. returning null.");
                    }
                } catch (Exception e) {
                    i.decrementAndGet();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        System.out.println();
        while(i.get() != 0) System.out.print(".");
        return gMapsRoads;
    }

    private void setRoads(GMapsRoads roads){
        gMapsRoads = roads;
    }
}