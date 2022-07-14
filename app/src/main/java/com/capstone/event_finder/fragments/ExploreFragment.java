package com.capstone.event_finder.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.event_finder.R;
import com.capstone.event_finder.activities.MainActivity;
import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.models.Bookmark;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.network.RetrofitClient;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment {

    private static final int POINTS_FOR_BOOKMARK_IN_CATEGORY_OF_INTEREST = 3;
    private static final int POINTS_FOR_CATEGORY_OF_INTEREST = 1;
    private static final int POINTS_FOR_BOOKMARK_NOT_IN_CATEGORY_OF_INTEREST = 2;
    RecyclerView rvRecommendations;
    EventsAdapter recommendationAdapter;
    private List<Event> recommendationList = new ArrayList<>();
    private JSONArray interestedCategories = new JSONArray();
    private List<Bookmark> bookmarksList = new ArrayList<>();
    private Set<String> bookmarkCategories = new HashSet<String>();

    public ExploreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView(view);
        bookmarksList.clear();
        bookmarkCategories.clear();
        ArrayList allBookmarkCategories = new ArrayList();
        ArrayList userInterestedCategories = new ArrayList();
        queryUserBookmarksFromParse(allBookmarkCategories, userInterestedCategories);
        interestedCategories = ParseUser.getCurrentUser().getJSONArray("event_categories");
        for (int i = 0; i < interestedCategories.length(); i++) {
            try {
                bookmarkCategories.add(interestedCategories.get(i).toString());
                userInterestedCategories.add(interestedCategories.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpRecyclerView(@NonNull View view) {
        rvRecommendations = view.findViewById(R.id.rvRecommendations);
        recommendationAdapter = new EventsAdapter(getContext(), recommendationList);
        rvRecommendations.setAdapter(recommendationAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvRecommendations.setLayoutManager(linearLayoutManager);
    }

    public void queryUserBookmarksFromParse(ArrayList allBookmarkCategories, ArrayList userInterestedCategories) {
        final int POST_LIMIT = 10;
        ParseQuery<Bookmark> query = ParseQuery.getQuery(Bookmark.class);
        query.whereEqualTo(Bookmark.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder("createdAt");
        query.findInBackground((bookmarks, e) -> {
            if (e != null) {
                Toast.makeText(getContext(), "Failed to find bookmarks", Toast.LENGTH_LONG).show();
                return;
            }
            bookmarksList.clear();
            bookmarksList.addAll(bookmarks);
            queryUserBookmarkCategories(allBookmarkCategories, userInterestedCategories);
        });
    }

    private void queryUserBookmarkCategories(ArrayList allBookmarkCategories, ArrayList userInterestedCategories) {
        for (int i = 0; i < bookmarksList.size(); i++) {
            Bookmark bookmark = bookmarksList.get(i);
            String bookmarkedEventCategory = bookmark.getEventCategory();
            allBookmarkCategories.add(bookmarkedEventCategory);
            bookmarkCategories.add(bookmarkedEventCategory);
        }
        calculatePoints(allBookmarkCategories, userInterestedCategories);
    }

    private void calculatePoints(ArrayList allBookmarkCategories, ArrayList userInterestedCategories) {
        Double totalBookmarks = 0.0 + allBookmarkCategories.size();
        HashMap<String, Double> categoryCount = new HashMap<String, Double>();
        Double totalPoints = 0.0;
        for (String category : bookmarkCategories) {
            int occurrences = Collections.frequency(allBookmarkCategories, category);
            boolean isUserInterestedCategory = userInterestedCategories.contains(category);
            Double points = 0.0;
            if (isUserInterestedCategory) {
                points += occurrences * POINTS_FOR_BOOKMARK_IN_CATEGORY_OF_INTEREST;
                points += POINTS_FOR_CATEGORY_OF_INTEREST;
            }
            else {
                points += occurrences * POINTS_FOR_BOOKMARK_NOT_IN_CATEGORY_OF_INTEREST;
            }
            totalPoints += points;
            categoryCount.put(category, points);
        }

        calculateEventsOfEachCategoryToQuery(categoryCount, totalPoints);
    }

    private void calculateEventsOfEachCategoryToQuery(HashMap<String, Double> categoryCount, Double totalPoints) {
        for (String category : bookmarkCategories) {
            Integer count = (int) (Math.floor(10 * (categoryCount.get(category) / totalPoints)));
            categoryCount.put(category, Math.floor(10 * (categoryCount.get(category) / totalPoints)));
            getAPIEvents(category, Integer.toString(count));
        }
        Log.d(TAG, "Category count : " + categoryCount.toString());
    }

    public void getAPIEvents(String category, String numberOfEventsToRetrieve) {
        String eventSearchRegion = "en_US";
        Long eventSearchRadiusFromUserInMeters = 40000L;
        Long upcomingEventsOnly = (System.currentTimeMillis() / 1000L);
        RetrofitClient.getInstance().getYelpAPI().getEvents(eventSearchRegion,
                numberOfEventsToRetrieve,
                upcomingEventsOnly,
                eventSearchRadiusFromUserInMeters,
                category,
                ParseUser.getCurrentUser().getString("zip")).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, response.toString());
                } else {
                    Toast.makeText(getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failed to get events", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private Collection<? extends Event> convertToList(JsonArray result) {
//        List<Event> res = new ArrayList<>();
//        for (int i = 0; i < result.size(); i++) {
//            JsonObject temp = (JsonObject) result.get(i);
//            Event event = new Event();
//            ((MainActivity) requireActivity()).populateEventInfo(event, temp);
//            res.add(event);
//        }
//        return res;
//    }
}