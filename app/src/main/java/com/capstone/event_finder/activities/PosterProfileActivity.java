package com.capstone.event_finder.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.capstone.event_finder.R;
import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.models.Photo;
import com.capstone.event_finder.network.EventViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PosterProfileActivity extends AppCompatActivity {

    private final List<Event> bookmarkList = new ArrayList<>();
    RecyclerView rvBookmarks;
    TextView tvProfileUsername;
    TextView tvProfileBio;
    TextView tvInterestCategories;
    ImageView ivProfileImage;
    EventsAdapter bookmarkAdapter;
    Photo photo;
    private EventViewModel eventViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_profile);

        photo = Parcels.unwrap(getIntent().getParcelableExtra(Photo.class.getSimpleName()));
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        tvProfileUsername = findViewById(R.id.tvProfileUsername);
        tvProfileBio = findViewById(R.id.tvProfileBio);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvInterestCategories = findViewById(R.id.tvInterestCategories);
        rvBookmarks = findViewById(R.id.rvBookmarks);

        tvProfileUsername.setText(photo.getUser().getUsername());
        tvProfileBio.setText(photo.getUser().getString("bio"));
        tvInterestCategories.setText(photo.getUser().getString("event_categories_string"));
        Glide.with(getApplicationContext()).load(Objects.requireNonNull(photo.getUser().getParseFile("profile_image")).getUrl()).centerCrop().transform(new CenterCrop(), new CircleCrop()).into(ivProfileImage);

        setUpRecyclerView();

        eventViewModel.getBookmarks(photo.getUser()).observe(this, events -> {
            bookmarkList.clear();
            bookmarkList.addAll(events);
            bookmarkAdapter.notifyDataSetChanged();
        });
    }

    private void setUpRecyclerView() {
        rvBookmarks = findViewById(R.id.rvBookmarks);
        bookmarkAdapter = new EventsAdapter(getApplicationContext(), bookmarkList);
        rvBookmarks.setAdapter(bookmarkAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvBookmarks.setLayoutManager(linearLayoutManager);
    }
}
