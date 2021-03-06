package com.example.chiilek.parkme.api_controllers.availability_api;

import com.example.chiilek.parkme.data_classes.availability_classes.Envelope;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chiilek on 23/3/2018.
 */

public interface CarParkAvailabilityAPI {

    // Our API call to pass current datetime in string format
    @GET("carpark-availability")
    Call<Envelope> getAvailability(@Query(value = "date_time", encoded = true) String datetime);
}