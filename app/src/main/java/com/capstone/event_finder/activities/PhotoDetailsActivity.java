package com.capstone.event_finder.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.capstone.event_finder.R;
import com.capstone.event_finder.models.Photo;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PhotoDetailsActivity extends AppCompatActivity {
    Photo photo;
    TextView tvPhotoDetailsUsername;
    TextView tvPhotoDetailsDescription;
    ImageView ivPhotoDetailsImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        tvPhotoDetailsUsername = (TextView) findViewById(R.id.tvPhotoDetailsUsername);
        tvPhotoDetailsDescription = (TextView) findViewById(R.id.tvPhotoDetailsDescription);
        ivPhotoDetailsImage = (ImageView) findViewById(R.id.ivPhotoDetailsImage);

        photo = (Photo) Parcels.unwrap(getIntent().getParcelableExtra(Photo.class.getSimpleName()));
        tvPhotoDetailsUsername.setText(photo.getUser().getUsername());
        tvPhotoDetailsDescription.setText(photo.getDescription());
        ParseFile image = photo.getImage();
        if (image != null) {
            Glide.with(getApplicationContext()).load(image.getUrl()).into(ivPhotoDetailsImage);
        }
    }

}

