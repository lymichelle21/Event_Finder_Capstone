package com.capstone.event_finder.network;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.capstone.event_finder.interfaces.EventDao;
import com.capstone.event_finder.models.Event;

@Database(entities = {Event.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {
    private static volatile EventDatabase instance;
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new populateDbAsyncTask(instance);
        }
    };

    public static synchronized EventDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (EventDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    EventDatabase.class, "event_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract EventDao eventDao();

    private static class populateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final EventDao eventDao;

        public populateDbAsyncTask(EventDatabase eventDatabase) {
            eventDao = eventDatabase.eventDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventDao.deleteAllEvents();
            return null;
        }
    }
}
