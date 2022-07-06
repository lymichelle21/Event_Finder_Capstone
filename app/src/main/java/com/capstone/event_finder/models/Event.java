package com.capstone.event_finder.models;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Generated;

@Entity(tableName = "event_table")
@Parcel(analyze = {Event.class})
@Generated("jsonschema2pojo")
public class Event {
    @ColumnInfo
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id;

    @ColumnInfo
    @SerializedName("attending_count")
    @Expose
    private int attendingCount;

    @ColumnInfo
    @SerializedName("category")
    @Expose
    private String category;

    @ColumnInfo
    @SerializedName("cost")
    @Expose
    private String cost;

    @ColumnInfo
    @SerializedName("cost_max")
    @Expose
    private String costMax;

    @ColumnInfo
    @SerializedName("description")
    @Expose
    private String description;

    @ColumnInfo
    @SerializedName("event_site_url")
    @Expose
    private String eventSiteUrl;

    @ColumnInfo
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @ColumnInfo
    @SerializedName("interested_count")
    @Expose
    private int interestedCount;

    @ColumnInfo
    @SerializedName("is_canceled")
    @Expose
    private boolean isCanceled;

    @ColumnInfo
    @SerializedName("is_free")
    @Expose
    private boolean isFree;

    @ColumnInfo
    @SerializedName("is_official")
    @Expose
    private boolean isOfficial;

    @ColumnInfo
    @SerializedName("latitude")
    @Expose
    private float latitude;

    @ColumnInfo
    @SerializedName("longitude")
    @Expose
    private float longitude;

    @ColumnInfo
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo
    @SerializedName("tickets_url")
    @Expose
    private String ticketsUrl;

    @ColumnInfo
    @SerializedName("time_end")
    @Expose
    private String timeEnd;

    @ColumnInfo
    @SerializedName("time_start")
    @Expose
    private String timeStart;

    @ColumnInfo
    @SerializedName("location")
    @Expose
    private String location;

    @ColumnInfo
    @SerializedName("business_id")
    @Expose
    private String businessId;

    public Event() {
    }

    public int getAttendingCount() {
        return attendingCount;
    }

    public void setAttendingCount(int attendingCount) {
        this.attendingCount = attendingCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = (String) cost;
    }

    public String getCostMax() {
        return costMax;
    }

    public void setCostMax(String costMax) {
        this.costMax = (String) costMax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventSiteUrl() {
        return eventSiteUrl;
    }

    public void setEventSiteUrl(String eventSiteUrl) {
        this.eventSiteUrl = eventSiteUrl;
    }

    public String getId() {
        return (String) id;
    }

    public void setId(String id) {
        this.id = (String) id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getInterestedCount() {
        return interestedCount;
    }

    public void setInterestedCount(int interestedCount) {
        this.interestedCount = interestedCount;
    }

    public boolean isIsCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public boolean isIsFree() {
        return isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public boolean isIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicketsUrl() {
        return ticketsUrl;
    }

    public void setTicketsUrl(String ticketsUrl) {
        this.ticketsUrl = ticketsUrl;
    }

    public String getTimeEnd(){
        try{
            return convertEventDateFormat(timeEnd);
        }
        catch (ParseException e) {
        }
        return "";
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeStart() {
        try{
            return convertEventDateFormat(timeStart);
        }
        catch (ParseException e) {
        }
        return "";
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getLocation() {
        return location.toString();
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    private String convertEventDateFormat(String unformattedDate) throws ParseException {
        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date formattedDate = inputFormat.parse(unformattedDate);
        return outputFormat.format(formattedDate);
    }
}


