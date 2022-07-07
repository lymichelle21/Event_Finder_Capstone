package com.capstone.event_finder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.network.RetrofitClient;
import com.capstone.event_finder.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {

    RecyclerView rvEvents;
    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter;

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView(view);
        getAPIEvents();
    }

    private void setUpRecyclerView(@NonNull View view) {
        rvEvents = view.findViewById(R.id.rvEvents);
        eventsList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(getContext(), eventsList);
        rvEvents.setAdapter(eventsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvEvents.setLayoutManager(linearLayoutManager);
    }

    private void getAPIEvents() {
        String eventSearchRegion = "en_US";
        Long eventSearchRadiusFromUserInMeters = 40000L;
        String numberOfEventsToRetrieve = "10";
        Long upcomingEventsOnly = (System.currentTimeMillis() / 1000L);
        RetrofitClient.getInstance().getYelpAPI().getEvents(eventSearchRegion,
                numberOfEventsToRetrieve,
                upcomingEventsOnly,
                eventSearchRadiusFromUserInMeters,
                ParseUser.getCurrentUser().getString("zip")).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            JsonObject result = response.body();
                            eventsList.clear();
                            eventsList.addAll(convertToList(result));
                            eventsAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "JSON exception error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failed to call API", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void populateEventInfo(Event event, JsonObject temp) {
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

    private void checkAndSetEventCost(Event event, JsonObject temp) {
        if (String.valueOf(temp.get("cost")) == "null") {
            event.setCost("$" + temp.get("cost").getAsString() + "0");
        } else {
            event.setCost("N/A");
        }
    }

    private void checkAndSetEventEndTime(Event event, JsonObject temp) {
        if (String.valueOf(temp.get("time_end")) == "null") {
            event.setTimeEnd(temp.get("time_end").getAsString());
        } else {
            event.setTimeEnd(temp.get("time_start").getAsString());
        }
    }

    private void formatAndSetEventLocation(JsonObject temp, Event event) {
        String formattedLocation = temp.get("location").getAsJsonObject().get("display_address").toString();
        formattedLocation = formattedLocation.replaceAll("[\\[\\]\"\"]", " ");
        event.setLocation(formattedLocation);
    }
}