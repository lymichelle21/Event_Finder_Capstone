package com.example.event_finder_capstone;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EventEndpointsInterface {
    @GET("events")
    Call<JsonObject> getEvents(@Query("locale") String locale, @Query("limit") String limit, @Query("location") String location);
}
