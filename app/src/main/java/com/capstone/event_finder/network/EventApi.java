package com.capstone.event_finder.network;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.capstone.event_finder.R;
import com.capstone.event_finder.activities.ErrorActivity;
import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.fragments.ExploreFragment;
import com.capstone.event_finder.fragments.FeedFragment;
import com.capstone.event_finder.fragments.ProfileFragment;
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

                } else {
                    Toast.makeText(activity.getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(activity.getContext()).
                                    setIcon(R.mipmap.ic_error_round).
                                    setTitle("Oh no!").
                                    setMessage("There are no events currently near you!").
                                    setPositiveButton("Change Zip", (dialog, which) -> {
                                        dialog.dismiss();
                                        Intent intent = new Intent(activity.getContext(), ErrorActivity.class);
                                        activity.startActivity(intent);
                                    });
                    builder.create().show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(activity.getContext(), "Failed to get events", Toast.LENGTH_SHORT).show();
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
            try {
                JsonObject result = response.body();
                assert result != null;
                eventRepository.deleteAllEvents();
                eventRepository.insert((List<Event>) convertToList(result));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity.getContext(), "JSON exception error", Toast.LENGTH_SHORT).show();
            }
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

    //TODO: Fix this for explore fragment
    private MutableLiveData<List<Event>> recommendationsResponseLiveData;

    public LiveData<List<Event>> getRecommendedEventsFromApi(List<Event> recommendationList, String category, String numberOfEventsToRetrieve, ExploreFragment activity){
        if (recommendationsResponseLiveData == null) {
            recommendationsResponseLiveData = new MutableLiveData<>();
        }
        String eventSearchRegion = "en_US";
        Long eventSearchRadiusFromUserInMeters = 40000L;
        Long upcomingEventsOnly = (System.currentTimeMillis() / 1000L);
        RetrofitClient.getInstance().getYelpAPI().getEvents(eventSearchRegion,
                numberOfEventsToRetrieve,
                upcomingEventsOnly,
                eventSearchRadiusFromUserInMeters,
                category,
                ParseUser.getCurrentUser().getString("zip")).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<JsonObject> call, @androidx.annotation.NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    activity.requireActivity().runOnUiThread(() -> {
                        try {
                            JsonObject result = response.body();
                            //Log.d(TAG, "response " + result.toString());
                            recommendationList.addAll(convertToList(result));
                            Log.d(TAG, "api " + recommendationList.toString());
                            recommendationsResponseLiveData.postValue(recommendationList);
                        } catch (Exception e) {
                            Log.e("error", "JSON exception error");
                        }
                    });
                } else {
                    recommendationsResponseLiveData.postValue(null);
                    Toast.makeText(activity.getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<JsonObject> call, @androidx.annotation.NonNull Throwable t) {
                Toast.makeText(activity.getContext(), "Failed to get events", Toast.LENGTH_SHORT).show();
            }
        });

        //Log.d(TAG, recommendationList.toString());
        return recommendationsResponseLiveData;
    }

    // TODO: fix for Profile
    public List<Event> lookupEventsAndSetEvents(List<Event> bookmarkList, String bookmarkId, JsonArray allBookmarks, ProfileFragment activity) {
        RetrofitClient.getInstance().getYelpAPI().lookupEvent(bookmarkId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<JsonObject> call, @androidx.annotation.NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject result = response.body();
                    allBookmarks.add(result);
                } else {
                    Toast.makeText(activity.getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
                }
                bookmarkList.addAll(convertToBookmarkList(allBookmarks));
                //bookmarkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<JsonObject> call, @androidx.annotation.NonNull Throwable t) {
                Toast.makeText(activity.getContext(), "Failed to get events", Toast.LENGTH_SHORT).show();
            }
        });

//        RetrofitClient.getInstance().getYelpAPI().lookupEvent(bookmarkId).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(@androidx.annotation.NonNull Call<JsonObject> call, @androidx.annotation.NonNull Response<JsonObject> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    JsonObject result = response.body();
//                    allBookmarks.add(result);
//                    bookmarkList.addAll(convertToBookmarkList(allBookmarks));
//                } else {
//                    Toast.makeText(activity.getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
//                }
//                //bookmarkList.addAll(convertToBookmarkList(allBookmarks));
//                //bookmarkAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(@androidx.annotation.NonNull Call<JsonObject> call, @androidx.annotation.NonNull Throwable t) {
//                Toast.makeText(activity.getContext(), "Failed to get events", Toast.LENGTH_SHORT).show();
//            }
//        });
//        Log.d(TAG, "hello " + bookmarkList.toString());

        return bookmarkList;
    }

    private Collection<? extends Event> convertToBookmarkList(JsonArray result) {
        List<Event> res = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            JsonObject temp = (JsonObject) result.get(i);
            Event event = new Event();
            populateEventInfo(event, temp);
            res.add(event);
        }
        return res;
    }
}
