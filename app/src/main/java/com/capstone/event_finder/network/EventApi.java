package com.capstone.event_finder.network;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;

import com.capstone.event_finder.R;
import com.capstone.event_finder.activities.ErrorActivity;
import com.capstone.event_finder.models.Event;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventApi {

    private final EventRepository eventRepository;

    public EventApi(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public LiveData<List<Event>> getAPIEvents() {
        String eventSearchRegion = "en_US";
        Long eventSearchRadiusFromUserInMeters = 40000L;
        String numberOfEventsToRetrieve = "10";
        Long upcomingEventsOnly = (System.currentTimeMillis() / 1000L);
        RetrofitClient.getInstance().getYelpAPI().getEvents(eventSearchRegion,
                numberOfEventsToRetrieve,
                upcomingEventsOnly,
                eventSearchRadiusFromUserInMeters,
                null,
                ParseUser.getCurrentUser().getString("zip")).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null && !String.valueOf(convertToList(response.body())).equals("[]")) {
                    addEventsToDatabase(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return null;
    }

    private Collection<? extends Event> convertToList(JsonObject result) {
        List<Event> res = new ArrayList<>();
        JsonArray events = result.getAsJsonArray("events");
        for (int i = 0; i < events.size(); i++) {
            JsonObject temp = (JsonObject) events.get(i);
            Event event = new Event();
            populateEventInfo(event, temp);
            res.add(event);
        }
        return res;
    }

    public void populateEventInfo(Event event, JsonObject temp) {
        event.setName(temp.get("name").getAsString());
        event.setDescription(temp.get("description").getAsString());
        event.setImageUrl(temp.get("image_url").getAsString());
        event.setTimeStart(temp.get("time_start").getAsString());
        event.setId(temp.get("id").getAsString());
        event.setEventSiteUrl(temp.get("event_site_url").getAsString());
        event.setCategory(temp.get("category").getAsString());
        checkAndSetEventEndTime(event, temp);
        checkAndSetEventCost(event, temp);
        formatAndSetEventLocation(temp, event);
    }

    private void addEventsToDatabase(@NonNull Response<JsonObject> response) {
        try {
            JsonObject result = response.body();
            assert result != null;
            eventRepository.deleteAllEvents();
            eventRepository.insert((List<Event>) convertToList(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAndSetEventCost(Event event, JsonObject temp) {
        if (temp.get("cost").toString().matches("null")) {
            event.setCost("N/A");
        } else {
            event.setCost("$" + temp.get("cost").getAsString() + "0");
        }
    }

    private void checkAndSetEventEndTime(Event event, JsonObject temp) {
        if (temp.get("time_end").toString().matches("null")) {
            event.setTimeEnd(temp.get("time_start").getAsString());
        } else {
            event.setTimeEnd(temp.get("time_end").getAsString());
        }
    }

    private void formatAndSetEventLocation(JsonObject temp, Event event) {
        JsonArray formattedLocation = temp.getAsJsonObject("location").getAsJsonArray("display_address");
        StringBuilder formattedLocationString = new StringBuilder();
        for (int i = 0; i < formattedLocation.size(); i++) {
            formattedLocationString.append(formattedLocation.get(i).getAsString()).append(" ");
        }
        event.setLocation(formattedLocationString.toString());
    }
}
