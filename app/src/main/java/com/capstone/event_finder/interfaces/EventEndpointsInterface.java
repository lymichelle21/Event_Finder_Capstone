package com.capstone.event_finder.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventEndpointsInterface {
    @GET("events")
    Call<JsonObject> getEvents(@Query("locale") String locale,
                               @Query("limit") String limit,
                               @Query("start_date") Long start_date,
                               @Query("radius") Long radius,
                               @Query("categories") String category,
                               @Query("location") String location);

    @GET("events/{id}")
    Call<JsonObject> lookupEvent(@Path("id") String id);
}
