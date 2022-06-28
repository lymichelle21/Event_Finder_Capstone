package com.example.event_finder_capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.event_finder_capstone.models.Event;

import org.parceler.Parcels;

public class PhotoAlbumActivity extends AppCompatActivity {

    private static final String TAG = "PhotoAlbumActivity";
    Context context;
    Event event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnAddPhoto) {
            onAddImageButton();
        }
        return true;
    }

    void onAddImageButton() {
        Intent intent = new Intent(PhotoAlbumActivity.this, AddPhotoActivity.class);
        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
        intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
