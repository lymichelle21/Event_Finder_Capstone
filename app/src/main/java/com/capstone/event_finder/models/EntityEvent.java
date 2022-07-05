package com.capstone.event_finder.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "event_table")
public class EntityEvent {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "eventTitle")
    private String eventTitle;
    @ColumnInfo(name = "eventDescription")
    private String eventDescription;
    @ColumnInfo(name = "startDate")
    private String startDate;
    @ColumnInfo(name = "endDate")
    private String endDate;
    @ColumnInfo(name = "eventPhotoLink")
    private String eventPhotoLink;
    @ColumnInfo(name = "eventAddress")
    private String eventAddress;
    @ColumnInfo(name = "eventSite")
    private String eventSite;
    @ColumnInfo(name = "eventCost")
    private String eventCost;

    public EntityEvent(String eventTitle, String eventDescription, String startDate,
                       String endDate, String eventPhotoLink, String eventAddress,
                       String eventSite, String eventCost) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventPhotoLink = eventPhotoLink;
        this.eventAddress = eventAddress;
        this.eventSite = eventSite;
        this.eventCost = eventCost;
    }
}
