package com.capstone.event_finder.network;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.capstone.event_finder.models.Bookmark;
import com.capstone.event_finder.models.Event;
import com.google.gson.JsonArray;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository eventRepository;
    private final EventApi eventApi;
    public LiveData<List<Event>> getEvents;
    public LiveData<List<Event>> eventInCache;
    private final List<Bookmark> userBookmarks = new ArrayList<>();
    private final MutableLiveData<List<Event>> bookmarkList;
    private final ArrayList<String> bookmarkIds = new ArrayList<>();

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepository = new EventRepository(application);
        bookmarkList = new MutableLiveData<>();
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

    public LiveData<List<Event>> getBookmarks() {
        JsonArray allBookmarks = new JsonArray();
        queryUserBookmarksFromParse(allBookmarks);
        return bookmarkList;
    }

    private void tryRetrieveEventInCache(String eventId, JsonArray allBookmarks) {

//        eventInCache(eventId, events -> {
//            if (events.isEmpty()) {
//                eventApi.lookupEventsAndSetEvents(eventId, allBookmarks, calledEvents -> {
//                    bookmarkList.postValue(calledEvents);
//                });
//            }
//            bookmarkList.postValue(events);
//        });

        eventApi.lookupEventsAndSetEvents(eventId, allBookmarks, events -> {
            bookmarkList.postValue(events);
        });
    }

    public void queryUserBookmarksFromParse(JsonArray allBookmarks) {
        final int POST_LIMIT = 10;
        ParseQuery<Bookmark> query = ParseQuery.getQuery(Bookmark.class);
        query.whereEqualTo(Bookmark.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder("createdAt");
        query.findInBackground((bookmarks, e) -> {
            if (e != null) {
                //Toast.makeText(getContext(), "Failed to find bookmarks", Toast.LENGTH_LONG).show();
                return;
            }
            userBookmarks.clear();
            userBookmarks.addAll(bookmarks);
            queryUserBookmarkIds(allBookmarks);
        });
    }

    private void queryUserBookmarkIds(JsonArray allBookmarks) {
        for (int i = 0; i < userBookmarks.size(); i++) {
            Bookmark bookmark = userBookmarks.get(i);
            String bookmarkId = bookmark.getEventId();
            bookmarkIds.add(bookmarkId);
        }
        queryAndSetUserBookmarksToFeed(allBookmarks);
    }

    private void queryAndSetUserBookmarksToFeed(JsonArray allBookmarks) {
        for (int i = 0; i < bookmarkIds.size(); i++) {
            tryRetrieveEventInCache(bookmarkIds.get(i), allBookmarks);
        }
    }
}


