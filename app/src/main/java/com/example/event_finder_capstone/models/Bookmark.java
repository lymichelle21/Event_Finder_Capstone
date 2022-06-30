package com.example.event_finder_capstone.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Bookmark")
public class Bookmark extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_BOOKMARKED_EVENT_ID = "eventId";
    public static final String KEY_BOOKMARKED_CATEGORY = "category";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getEventId() {
        return getString(KEY_BOOKMARKED_EVENT_ID);
    }

    public void setEventId(String eventId) {
        put(KEY_BOOKMARKED_EVENT_ID, eventId);
    }

    public String getEventCategory() {
        return getString(KEY_BOOKMARKED_CATEGORY);
    }

    public void setEventCategory(String eventCategory) {
        put(KEY_BOOKMARKED_CATEGORY, eventCategory);
    }
}
