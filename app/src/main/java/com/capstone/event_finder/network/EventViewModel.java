package com.capstone.event_finder.network;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.capstone.event_finder.models.Event;

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

    public LiveData<List<Event>> eventInCache(String eventId) {
        return eventRepository.eventInCache(eventId);
    }
}
