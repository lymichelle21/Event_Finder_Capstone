package com.capstone.event_finder.network;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.capstone.event_finder.models.Event;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository eventRepository;
    private final EventApi eventApi;
    public LiveData<List<Event>> getEvents;
    public LiveData<List<Event>> eventInCache;

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepository = new EventRepository(application);
        eventApi = new EventApi();
        getEvents = eventRepository.getEvents();
        eventInCache = eventInCache("");
    }

    public void clearCache() {
        eventRepository.deleteAllEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return getEvents;
    }

    public void refreshEvents() {
        eventApi.getAPIEvents(events -> {
            eventRepository.deleteAllEvents();
            eventRepository.insert(events);
        });
    }

    public LiveData<List<Event>> eventInCache(String eventId) {
        return eventRepository.eventInCache(eventId);
    }
}


