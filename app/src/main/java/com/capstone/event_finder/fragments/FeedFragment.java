package com.capstone.event_finder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.capstone.event_finder.R;
import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.network.EventViewModel;

import java.util.ArrayList;
import java.util.List;

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
        getEvents();
    }

    private void setUpSwipeRefresh(@NonNull View view) {
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> {
            Toast.makeText(getContext(), "Finding new events!", Toast.LENGTH_SHORT).show();
            eventViewModel.refreshEvents(eventsList, FeedFragment.this);
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

    private void getEvents() {
        eventViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            eventsList.addAll(events);
            eventsAdapter.notifyDataSetChanged();
        });
    }
}