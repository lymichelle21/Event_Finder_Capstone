//package com.capstone.event_finder.models;
//
//import android.icu.text.DateFormat;
//import android.icu.text.SimpleDateFormat;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.text.ParseException;
//import java.util.Date;
//import java.util.Locale;
//
//@Entity(tableName = "event_table")
//public class EventEntity {
//    @ColumnInfo
//    @PrimaryKey
//    @SerializedName("id")
//    @Expose
//    private String id;
//    @ColumnInfo
//    @SerializedName("category")
//    @Expose
//    private String category;
//
//    @SerializedName("cost")
//    @Expose
//    private String cost;
//
//    @SerializedName("description")
//    @Expose
//    private String description;
//    @SerializedName("event_site_url")
//    @Expose
//    private String eventSiteUrl;
//    @SerializedName("image_url")
//    @Expose
//    private String imageUrl;
//    @SerializedName("name")
//    @Expose
//    private String name;
//    @SerializedName("time_end")
//    @Expose
//    private String timeEnd;
//    @SerializedName("time_start")
//    @Expose
//    private String timeStart;
//    @SerializedName("location")
//    @Expose
//    private String location;
//
//    public EventEntity() {
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public String getCost() {
//        return cost;
//    }
//
//    public void setCost(String cost) {
//        this.cost = (String) cost;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getEventSiteUrl() {
//        return eventSiteUrl;
//    }
//
//    public void setEventSiteUrl(String eventSiteUrl) {
//        this.eventSiteUrl = eventSiteUrl;
//    }
//
//    public String getId() {
//        return (String) id;
//    }
//
//    public void setId(String id) {
//        this.id = (String) id;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//
//    public String getTimeEnd() throws ParseException {
//        return convertEventDateFormat(timeEnd);
//    }
//
//    public void setTimeEnd(String timeEnd) {
//        this.timeEnd = timeEnd;
//    }
//
//    public String getTimeStart() throws ParseException {
//        return convertEventDateFormat(timeStart);
//    }
//
//    public void setTimeStart(String timeStart) {
//        this.timeStart = timeStart;
//    }
//
//    public String getLocation() {
//        return location.toString();
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    private String convertEventDateFormat(String unformattedDate) throws ParseException {
//        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
//        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
//        Date formattedDate = inputFormat.parse(unformattedDate);
//        return outputFormat.format(formattedDate);
//    }
//
//    public static EventEntity(String eventTitle) {
//        this.eventTitle = eventTitle;
////        this.eventDescription = eventDescription;
////        this.startDate = startDate;
////        this.endDate = endDate;
////        this.eventPhotoLink = eventPhotoLink;
////        this.eventAddress = eventAddress;
////        this.eventSite = eventSite;
////        this.eventCost = eventCost;
//    }
//
//}
