package com.example.event_finder_capstone;

import com.example.event_finder_capstone.models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EventEndpointsInterface {
    @GET ("events")
    Call<Event> getEventId(@Path("id") String event_id);
    Call<Event> getEventName(@Path("name") String event_name);
    Call<Event> getEventDescription(@Path("description") String event_description);
    Call<Event> getEventSite(@Path("event_site_url") String event_site_url);

    Call<List<Event>> getEvents();
}
