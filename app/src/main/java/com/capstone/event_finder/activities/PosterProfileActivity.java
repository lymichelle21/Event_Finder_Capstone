package com.capstone.event_finder.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.capstone.event_finder.R;
import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.models.Photo;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Objects;


public class PosterProfileActivity extends AppCompatActivity {

    RecyclerView rvBookmarks;
    TextView tvProfileUsername;
    TextView tvProfileBio;
    TextView tvInterestCategories;
    ImageView ivProfileImage;
    Photo photo;
    //EventsAdapter bookmarkAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_profile);

        photo = Parcels.unwrap(getIntent().getParcelableExtra(Photo.class.getSimpleName()));

        tvProfileUsername = findViewById(R.id.tvProfileUsername);
        tvProfileBio = findViewById(R.id.tvProfileBio);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvInterestCategories = findViewById(R.id.tvInterestCategories);
        rvBookmarks = findViewById(R.id.rvBookmarks);

        tvProfileUsername.setText(photo.getUser().getUsername());
        tvProfileBio.setText(photo.getUser().getString("bio"));
        tvInterestCategories.setText(photo.getUser().getString("event_categories_string"));
        Glide.with(getApplicationContext()).load(Objects.requireNonNull(photo.getUser().getParseFile("profile_image")).getUrl()).centerCrop().transform(new CenterCrop(), new CircleCrop()).into(ivProfileImage);
    }

}
