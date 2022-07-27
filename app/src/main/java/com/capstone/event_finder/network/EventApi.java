package com.capstone.event_finder.network;

import android.support.annotation.NonNull;

import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.utils.SetEventInfo;
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

    public EventApi() {
    }

    public void getAPIEvents(GetAPIEventsHandler apiEventHandler) {
        String eventSearchRegion = "en_US";
        String numberOfEventsToRetrieve = "10";
        Long upcomingEventsOnly = (System.currentTimeMillis() / 1000L);
        Long eventSearchRadiusFromUserInMeters = 40000L;
        RetrofitClient.getInstance().getYelpAPI().getEvents(eventSearchRegion,
                numberOfEventsToRetrieve,
                upcomingEventsOnly,
                eventSearchRadiusFromUserInMeters,
                null,
                ParseUser.getCurrentUser().getString("zip")).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null && !convertToList(response.body()).isEmpty()) {
                    addEventsToDatabase(response, apiEventHandler);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Collection<? extends Event> convertToList(JsonObject result) {
        List<Event> res = new ArrayList<>();
        JsonArray events = result.getAsJsonArray("events");
        for (int i = 0; i < events.size(); i++) {
            JsonObject temp = (JsonObject) events.get(i);
            Event event = new Event();
            SetEventInfo.populateEvent(event, temp);
            res.add(event);
        }
        return res;
    }

    private Collection<? extends Event> convertBookmarksToList(JsonArray result) {
        List<Event> res = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            JsonObject temp = (JsonObject) result.get(i);
            Event event = new Event();
            SetEventInfo.populateEvent(event, temp);
            res.add(event);
        }
        return res;
    }

    private void addEventsToDatabase(@NonNull Response<JsonObject> response, GetAPIEventsHandler apiEventHandler) {
        try {
            JsonObject result = response.body();
            assert result != null;
            apiEventHandler.eventsReceived((List<Event>) convertToList(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lookupEventsAndSetEvents(String bookmarkId, JsonArray allBookmarks, GetAPIEventsHandler apiEventHandler) {
        RetrofitClient.getInstance().getYelpAPI().lookupEvent(bookmarkId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<JsonObject> call, @androidx.annotation.NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject result = response.body();
                    allBookmarks.add(result);
                    apiEventHandler.eventsReceived((List<Event>) convertBookmarksToList(allBookmarks));
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<JsonObject> call, @androidx.annotation.NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
