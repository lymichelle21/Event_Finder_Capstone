package com.capstone.event_finder.network;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.capstone.event_finder.fragments.FeedFragment;
import com.capstone.event_finder.interfaces.EventDao;
import com.capstone.event_finder.models.Event;

import java.util.List;

public class EventRepository {

    public EventDao eventDao;
    public EventApi eventApi;

    public EventRepository(Application application) {
        EventDatabase eventDatabase = EventDatabase.getInstance(application);
        eventDao = eventDatabase.eventDao();
        eventApi = new EventApi(this);
    }

    public void insert(List<Event> events) {
        new InsertAsyncTask(eventDao).execute(events);
    }

    public LiveData<List<Event>> getEvents() {
        return eventDao.getEvents();
    }

    public LiveData<List<Event>> eventInCache(String eventId) {
        return eventDao.eventInCache(eventId);
    }

    public void deleteAllEvents() {
        new DeleteAsyncTask(eventDao).execute();
    }

    public LiveData<List<Event>> getEventsFromApi(List<Event> eventsList, FeedFragment activity) {
        return eventApi.getAPIEvents(eventsList, activity);
    }

    private static class InsertAsyncTask extends AsyncTask<List<Event>, Void, Void> {
        private final EventDao eventDao;

        public InsertAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Event>... lists) {
            eventDao.insert(lists[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<List<Event>, Void, Void> {
        private final EventDao eventDao;

        public DeleteAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Event>... lists) {
            eventDao.deleteAllEvents();
            return null;
        }
    }


}
