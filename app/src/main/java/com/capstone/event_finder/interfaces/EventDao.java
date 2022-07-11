package com.capstone.event_finder.interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.capstone.event_finder.models.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Event> events);

    @Query("SELECT * FROM event_table")
    LiveData<List<Event>> getEvents();

    @Query("DELETE FROM event_table")
    void deleteAllEvents();
}
