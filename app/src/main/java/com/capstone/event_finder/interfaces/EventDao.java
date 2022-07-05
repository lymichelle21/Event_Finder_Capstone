package com.capstone.event_finder.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.capstone.event_finder.models.EventEntity;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event_table")
    List<EventEntity> getAll();

    @Insert
    void insert(EventEntity model);

    @Query("DELETE FROM event_table")
    void deleteAllEvents();
}
