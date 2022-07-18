package com.capstone.event_finder.network;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.capstone.event_finder.fragments.FeedFragment;
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

public class EventViewModel extends AndroidViewModel {

    private final EventRepository eventRepository;
    public LiveData<List<Event>> getEvents;
    public LiveData<List<Event>> eventInCache;
    public List<Event> eventsList = new ArrayList<>();

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepository = new EventRepository(application);
        getEvents = eventRepository.getEvents();
        eventInCache = eventInCache("");
    }

    public void insert(List<Event> events) {
        eventRepository.insert(events);
    }

    public void delete() {
        eventRepository.deleteAllEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return getEvents;
    }

    public LiveData<List<Event>> getEventsFromApi(List<Event> eventsList, FeedFragment activity) {
        return getAPIEvents(eventsList, activity);
    }

    public LiveData<List<Event>> eventInCache(String eventId) {
        return eventRepository.eventInCache(eventId);
    }

    public LiveData<List<Event>> getAPIEvents(List<Event> eventsList, FeedFragment activity) {
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
                    addEventsToDatabase(response, activity);
                    Log.d(TAG, "dino " + response.body().toString());
                } else {
                    //Toast.makeText(context, "Query Failed", Toast.LENGTH_SHORT).show();
//                    AlertDialog.Builder builder =
//                            new AlertDialog.Builder(activity.getContext()).
//                                    setIcon(R.mipmap.ic_error_round).
//                                    setTitle("Oh no!").
//                                    setMessage("There are no events currently near you!").
//                                    setPositiveButton("Change Zip", (dialog, which) -> {
//                                        dialog.dismiss();
//                                        Intent intent = new Intent(activity, ErrorActivity.class);
//                                        startActivity(intent);
//                                    });
//                    builder.create().show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                //Toast.makeText(activity, "Failed to get events", Toast.LENGTH_SHORT).show();
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

        private void addEventsToDatabase(@NonNull Response<JsonObject> response, FeedFragment activity) {
        if (activity != null) {
//            activity.runOnUiThread(() -> {
                try {
                    JsonObject result = response.body();
                    //eventsList.clear();
                    assert result != null;
                    delete();
                    insert((List<Event>) convertToList(result));
                    //eventsAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    //Toast.makeText(getContext(), "JSON exception error", Toast.LENGTH_SHORT).show();
                }
//            });
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


