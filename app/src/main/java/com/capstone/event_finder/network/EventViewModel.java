package com.capstone.event_finder.network;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.capstone.event_finder.models.Event;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    public LiveData<List<Event>> getEvents;
    private final EventRepository eventRepository;

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepository = new EventRepository(application);
        getEvents = eventRepository.getEvents();
    }

    public void insert(List<Event> events) {
        eventRepository.insert(events);
    }

    public void delete(List<Event> events) {
        eventRepository.deleteAllEvents(events);
    }

    public LiveData<List<Event>> getEvents() {
        return getEvents;
    }
}
