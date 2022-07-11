package com.capstone.event_finder.network;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.capstone.event_finder.interfaces.EventDao;
import com.capstone.event_finder.models.Event;

import java.util.List;

public class EventRepository {

    public EventDao eventDao;
    public LiveData<List<Event>> getEvents;

    public EventRepository(Application application) {
        EventDatabase eventDatabase = EventDatabase.getInstance(application);
        eventDao = eventDatabase.eventDao();
        getEvents = eventDao.getEvents();
    }

    public void insert(List<Event> events){
        new InsertAsyncTask(eventDao).execute(events);
    }

    public LiveData<List<Event>> getEvents(){
        return getEvents;
    }

    public void deleteAllEvents() {
        new DeleteAsyncTask(eventDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<List<Event>,Void,Void> {
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

    private static class DeleteAsyncTask extends AsyncTask<List<Event>,Void,Void> {
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
