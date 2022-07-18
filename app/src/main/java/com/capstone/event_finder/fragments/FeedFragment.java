package com.capstone.event_finder.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.capstone.event_finder.R;
import com.capstone.event_finder.activities.ErrorActivity;
import com.capstone.event_finder.activities.MainActivity;
import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.interfaces.EventFetcherInterface;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.network.EventViewModel;
import com.capstone.event_finder.network.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// implements EventFetcherInterface
public class FeedFragment extends Fragment {

    RecyclerView rvEvents;
    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter;
    private EventViewModel eventViewModel;
    private SwipeRefreshLayout swipeContainer;

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
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        setUpRecyclerView(view);
        setUpSwipeRefresh(view);
        tryLocallyCaching();
    }

    private void setUpSwipeRefresh(@NonNull View view) {
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> {
            Toast.makeText(getContext(), "Finding new events!", Toast.LENGTH_SHORT).show();
            //getAPIEvents();
            eventViewModel.getEventsFromApi(eventsList, FeedFragment.this);
            swipeContainer.setRefreshing(false);
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setUpRecyclerView(@NonNull View view) {
        rvEvents = view.findViewById(R.id.rvEvents);
        eventsList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(getContext(), eventsList);
        rvEvents.setAdapter(eventsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvEvents.setLayoutManager(linearLayoutManager);
    }
//
//    public void getAPIEvents() {
//        String eventSearchRegion = "en_US";
//        Long eventSearchRadiusFromUserInMeters = 40000L;
//        String numberOfEventsToRetrieve = "10";
//        Long upcomingEventsOnly = (System.currentTimeMillis() / 1000L);
//        RetrofitClient.getInstance().getYelpAPI().getEvents(eventSearchRegion,
//                numberOfEventsToRetrieve,
//                upcomingEventsOnly,
//                eventSearchRadiusFromUserInMeters,
//                null,
//                ParseUser.getCurrentUser().getString("zip")).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
//                if (response.isSuccessful() && response.body() != null && !String.valueOf(convertToList(response.body())).equals("[]")) {
//                    addEventsToDatabase(response);
//                } else {
//                    Toast.makeText(getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
//                    AlertDialog.Builder builder =
//                            new AlertDialog.Builder(requireContext()).
//                                    setIcon(R.mipmap.ic_error_round).
//                                    setTitle("Oh no!").
//                                    setMessage("There are no events currently near you!").
//                                    setPositiveButton("Change Zip", (dialog, which) -> {
//                                        dialog.dismiss();
//                                        Intent intent = new Intent(getContext(), ErrorActivity.class);
//                                        startActivity(intent);
//                                    });
//                    builder.create().show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
//                Toast.makeText(getContext(), "Failed to get events", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void addEventsToDatabase(@NonNull Response<JsonObject> response) {
//        Activity activity = getActivity();
//        if (activity != null) {
//            requireActivity().runOnUiThread(() -> {
//                try {
//                    JsonObject result = response.body();
//                    eventsList.clear();
//                    assert result != null;
//                    clearAndAddEventsToDatabase(result);
//                    eventsAdapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), "JSON exception error", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

    private void tryLocallyCaching() {
//        LiveData<List<Event>> x = eventViewModel.getEventsFromApi(eventsList, FeedFragment.this);
//        Log.d(TAG, "hiya " + String.valueOf(x));

        eventViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            Log.d(TAG, "what's up : " + events.toString());
            eventsList.addAll(events);
            eventsAdapter.notifyDataSetChanged();
        });
    }
//
//    private void clearAndAddEventsToDatabase(JsonObject result) {
//        eventViewModel.delete();
//        eventViewModel.insert((List<Event>) convertToList(result));
//    }
//
//    private Collection<? extends Event> convertToList(JsonObject result) {
//        List<Event> res = new ArrayList<>();
//        JsonArray events = result.getAsJsonArray("events");
//        for (int i = 0; i < events.size(); i++) {
//            JsonObject temp = (JsonObject) events.get(i);
//            Event event = new Event();
//            ((MainActivity) requireActivity()).populateEventInfo(event, temp);
//            res.add(event);
//        }
//        return res;
//    }
}