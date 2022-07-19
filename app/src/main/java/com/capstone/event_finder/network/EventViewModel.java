package com.capstone.event_finder.network;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.capstone.event_finder.fragments.ExploreFragment;
import com.capstone.event_finder.fragments.FeedFragment;
import com.capstone.event_finder.fragments.ProfileFragment;
import com.capstone.event_finder.models.Event;
import com.google.gson.JsonArray;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository eventRepository;
    public LiveData<List<Event>> getEvents;
    public LiveData<List<Event>> eventInCache;

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

    public LiveData<List<Event>> refreshEvents(List<Event> eventsList, FeedFragment activity) {
        return eventRepository.getEventsFromApi(eventsList, activity);
    }

    public LiveData<List<Event>> eventInCache(String eventId) {
        return eventRepository.eventInCache(eventId);
    }

    public List<Event> getRecommendations(List<Event> recommendationList, String category, String numberOfEventsToRetrieve, ExploreFragment activity) {
        return eventRepository.getRecommendedEventsFromApi(recommendationList, category, numberOfEventsToRetrieve, activity);
    }

    public List<Event> getBookmarkedEvents(List<Event> bookmarkList, String bookmarkId, JsonArray allBookmarks, ProfileFragment activity) {
        return eventRepository.getBookmarkedEvents(bookmarkList, bookmarkId, allBookmarks, activity);
    }

    // TODO: Start here
    public List<Event> tryRetrieveEventInCache(List<Event> bookmarkList, String eventId, JsonArray allBookmarks, ProfileFragment activity) {
        eventInCache(eventId).observe(activity.getViewLifecycleOwner(), events -> {
            if (events.isEmpty()) {
                //events = getBookmarkedEvents(bookmarkList, eventId, allBookmarks, activity);
                eventRepository.eventApi.lookupEventsAndSetEvents(bookmarkList, eventId, allBookmarks, activity);
            }
            bookmarkList.addAll(events);
            //bookmarkAdapter.notifyDataSetChanged();
        });
        return bookmarkList;
    }

}


