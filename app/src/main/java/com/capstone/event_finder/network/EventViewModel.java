package com.capstone.event_finder.network;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.capstone.event_finder.fragments.ExploreFragment;
import com.capstone.event_finder.fragments.FeedFragment;
import com.capstone.event_finder.fragments.ProfileFragment;
import com.capstone.event_finder.models.Bookmark;
import com.capstone.event_finder.models.Event;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository eventRepository;
    public LiveData<List<Event>> getEvents;
    public LiveData<List<Event>> eventInCache;
//    public List<Event> recommendationList = new ArrayList<>();
//    public HashMap<String, Double> categoryCount = new HashMap<>();

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepository = new EventRepository(application);
        getEvents = eventRepository.getEvents();
        eventInCache = eventInCache("");
    }

    public void clearCache() {
        eventRepository.deleteAllEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return getEvents;
    }

    public List<Event> refreshEvents(FeedFragment activity) {
        List<Event> eventsList = new ArrayList<>();
        eventRepository.getEventsFromApi(eventsList, activity);
        return eventsList;
    }

    public LiveData<List<Event>> eventInCache(String eventId) {
        return eventRepository.eventInCache(eventId);
    }

//    TODO: Fix this so ignore
//    public LiveData<List<Event>> getRecommendations(Set<String> userInterestedAndBookmarkedEventCategories, List<Bookmark> bookmarksList, ArrayList<String> userBookmarkedCategories, ArrayList<String> userInterestedCategories, ExploreFragment activity) {
//        queryUserBookmarksFromParse(userInterestedAndBookmarkedEventCategories, bookmarksList, userBookmarkedCategories, userInterestedCategories, activity);
//        return eventRepository.getRecommendedEventsFromApi(recommendationList, "", "", activity);
//    }
//
//    public List<Event> getBookmarkedEvents(List<Event> bookmarkList, String bookmarkId, JsonArray allBookmarks, ProfileFragment activity) {
//        return eventRepository.getBookmarkedEvents(bookmarkList, bookmarkId, allBookmarks, activity);
//    }
//
//    public void queryUserBookmarksFromParse(Set<String> userInterestedAndBookmarkedEventCategories, List<Bookmark> bookmarksList, ArrayList<String> userBookmarkedCategories, ArrayList<String> userInterestedCategories, ExploreFragment activity) {
//        includeUserInterestedCategories(userInterestedAndBookmarkedEventCategories, userInterestedCategories);
//        final int POST_LIMIT = 10;
//        ParseQuery<Bookmark> query = ParseQuery.getQuery(Bookmark.class);
//        query.whereEqualTo(Bookmark.KEY_USER, ParseUser.getCurrentUser());
//        query.setLimit(POST_LIMIT);
//        query.addDescendingOrder("createdAt");
//        query.findInBackground((bookmarks, e) -> {
//            if (e != null) {
//                Toast.makeText(activity.getContext(), "Failed to find bookmarks", Toast.LENGTH_LONG).show();
//                return;
//            }
//            bookmarksList.clear();
//            bookmarksList.addAll(bookmarks);
//            queryUserBookmarkCategories(userInterestedAndBookmarkedEventCategories,bookmarksList, userBookmarkedCategories, userInterestedCategories, activity);
//        });
//    }
//
//    private void queryUserBookmarkCategories(Set<String> userInterestedAndBookmarkedEventCategories, List<Bookmark> bookmarksList, ArrayList<String> userBookmarkedCategories, ArrayList<String> userInterestedCategories, ExploreFragment activity) {
//        for (int i = 0; i < bookmarksList.size(); i++) {
//            Bookmark bookmark = bookmarksList.get(i);
//            String bookmarkedEventCategory = bookmark.getEventCategory();
//            userBookmarkedCategories.add(bookmarkedEventCategory);
//            userInterestedAndBookmarkedEventCategories.add(bookmarkedEventCategory);
//        }
//        calculatePoints(userInterestedAndBookmarkedEventCategories, userBookmarkedCategories, userInterestedCategories, activity);
//    }
//
//    private void calculatePoints(Set<String> userInterestedAndBookmarkedEventCategories, ArrayList<String> userBookmarkedCategories, ArrayList<String> userInterestedCategories, ExploreFragment activity) {
//        final int POINTS_FOR_BOOKMARK_IN_CATEGORY_OF_INTEREST = 3;
//        final int POINTS_FOR_CATEGORY_OF_INTEREST = 1;
//        final int POINTS_FOR_BOOKMARK_NOT_IN_CATEGORY_OF_INTEREST = 2;
//        double totalPoints = 0.0;
//        for (String category : userInterestedAndBookmarkedEventCategories) {
//            int occurrences = Collections.frequency(userBookmarkedCategories, category);
//            boolean isUserInterestedCategory = userInterestedCategories.contains(category);
//            double points = 0.0;
//            if (isUserInterestedCategory) {
//                points += occurrences * POINTS_FOR_BOOKMARK_IN_CATEGORY_OF_INTEREST;
//                points += POINTS_FOR_CATEGORY_OF_INTEREST;
//            } else {
//                points += occurrences * POINTS_FOR_BOOKMARK_NOT_IN_CATEGORY_OF_INTEREST;
//            }
//            totalPoints += points;
//            categoryCount.put(category, points);
//        }
//        calculateEventsOfEachCategoryToQuery(userInterestedAndBookmarkedEventCategories, categoryCount, totalPoints, activity);
//    }
//
//
//    private void calculateEventsOfEachCategoryToQuery(Set<String> userInterestedAndBookmarkedEventCategories, HashMap<String, Double> categoryCount, Double totalPoints, ExploreFragment activity) {
//        recommendationList.clear();
//
//        Log.d(TAG, "category count in function " + categoryCount.toString());
//
//        for (String category : userInterestedAndBookmarkedEventCategories) {
//            int count = (int) (Math.ceil(10 * (categoryCount.get(category) / totalPoints)));
//            categoryCount.put(category, (double) 2);
//            eventRepository.getRecommendedEventsFromApi(recommendationList, category, Integer.toString(count), activity);
//        }
//    }
//
//    private void includeUserInterestedCategories(Set<String> userInterestedAndBookmarkedEventCategories, ArrayList<String> userInterestedCategories) {
//        JSONArray interestedCategories = ParseUser.getCurrentUser().getJSONArray("event_categories");
//        for (int i = 0; i < Objects.requireNonNull(interestedCategories).length(); i++) {
//            try {
//                userInterestedAndBookmarkedEventCategories.add(interestedCategories.get(i).toString());
//                userInterestedCategories.add(interestedCategories.get(i).toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public List<Event> tryRetrieveEventInCache(List<Event> bookmarkList, String eventId, JsonArray allBookmarks, ProfileFragment activity) {
//        eventInCache(eventId).observe(activity.getViewLifecycleOwner(), events -> {
//            if (events.isEmpty()) {
//                //events = getBookmarkedEvents(bookmarkList, eventId, allBookmarks, activity);
//                eventRepository.eventApi.lookupEventsAndSetEvents(bookmarkList, eventId, allBookmarks, activity);
//            }
//            bookmarkList.addAll(events);
//            //bookmarkAdapter.notifyDataSetChanged();
//        });
//        return bookmarkList;
//    }



}


