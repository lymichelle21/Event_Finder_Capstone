package com.example.event_finder_capstone;

import com.example.event_finder_capstone.models.Event;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventEndpointsInterface {
    @GET("events")
    Call<Event> getEvents();
}
