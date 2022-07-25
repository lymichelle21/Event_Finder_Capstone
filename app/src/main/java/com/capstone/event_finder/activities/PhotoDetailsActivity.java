package com.capstone.event_finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.capstone.event_finder.R;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.models.Photo;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PhotoDetailsActivity extends AppCompatActivity {
    Photo photo;
    TextView tvPhotoDetailsUsername;
    TextView tvPhotoDetailsDescription;
    ImageView ivPhotoDetailsImage;
    ImageView ivPosterProfileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        tvPhotoDetailsUsername = findViewById(R.id.tvPhotoDetailsUsername);
        tvPhotoDetailsDescription = findViewById(R.id.tvPhotoDetailsDescription);
        ivPhotoDetailsImage = findViewById(R.id.ivPhotoDetailsImage);
        ivPosterProfileImage = findViewById(R.id.ivPosterProfileImage);
        getAndSetPhotoContent();
        ivPosterProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPosterProfile();
            }
        });
    }

    private void viewPosterProfile() {
        Intent i = new Intent(this, PosterProfileActivity.class);
        i.putExtra(Photo.class.getSimpleName(), Parcels.wrap(photo));
        startActivity(i);
        finish();
    }

    private void getAndSetPhotoContent() {
        photo = Parcels.unwrap(getIntent().getParcelableExtra(Photo.class.getSimpleName()));
        tvPhotoDetailsUsername.setText(photo.getUser().getUsername());
        tvPhotoDetailsDescription.setText(photo.getDescription());
        ParseFile image = photo.getImage();
        if (image != null) {
            Glide.with(getApplicationContext()).load(image.getUrl()).into(ivPhotoDetailsImage);
        }
        ParseFile profileImage = (photo.getUser()).getParseFile("profile_image");
        if (image != null) {
            Glide.with(getApplicationContext()).load(profileImage.getUrl()).centerCrop().transform(new CenterCrop(), new CircleCrop()).into(ivPosterProfileImage);
        }
    }

}

