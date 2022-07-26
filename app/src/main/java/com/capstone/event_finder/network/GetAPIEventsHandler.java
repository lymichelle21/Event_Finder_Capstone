package com.capstone.event_finder.network;

import com.capstone.event_finder.models.Event;

import java.util.ArrayList;
import java.util.List;

public interface GetAPIEventsHandler {
    void eventsReceived(List<Event> events);
}
