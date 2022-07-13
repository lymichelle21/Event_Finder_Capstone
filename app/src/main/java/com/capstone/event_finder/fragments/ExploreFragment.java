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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExploreFragment extends Fragment {

    private List<Event> recommendationList = new ArrayList<>();
    private JSONArray interestedCategories = new JSONArray();
    private List<Bookmark> bookmarksList = new ArrayList<>();
    private Set<String> bookmarkCategories = new HashSet<String>();

    RecyclerView rvRecommendations;
    EventsAdapter recommendationAdapter;

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
        ArrayList allBookmarkCategories = new ArrayList();
        queryUserBookmarksFromParse(allBookmarkCategories);

        interestedCategories = ParseUser.getCurrentUser().getJSONArray("event_categories");
        for (int i =0; i < interestedCategories.length(); i++) {
            try {
                Log.d(TAG, (String) interestedCategories.get(i));
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

    public void queryUserBookmarksFromParse(ArrayList allBookmarkCategories) {
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
            queryUserBookmarkCategories(allBookmarkCategories);
        });
    }

    private void queryUserBookmarkCategories(ArrayList allBookmarkCategories) {
        for (int i = 0; i < bookmarksList.size(); i++) {
            Bookmark bookmark = bookmarksList.get(i);
            String bookmarkedEventCategory = bookmark.getEventCategory();
            allBookmarkCategories.add(bookmarkedEventCategory);
            bookmarkCategories.add(bookmarkedEventCategory);
        }
        getCategoryOccurrence(allBookmarkCategories);
    }

    private void getCategoryOccurrence(ArrayList allBookmarkCategories) {
        Log.d(TAG, bookmarkCategories.toString());
        Log.d(TAG, allBookmarkCategories.toString());
        for (String category : bookmarkCategories) {
            int occurrences = Collections.frequency(allBookmarkCategories, category);
            Log.d(TAG, category + " : " + String.valueOf(occurrences));
        }
    }


}