package com.example.event_finder_capstone.activities;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.event_finder_capstone.R;
import com.example.event_finder_capstone.models.Bookmark;
import com.example.event_finder_capstone.models.Event;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EventDetailsActivity extends AppCompatActivity {
    Event event;
    TextView tvEventDetailsTitle;
    TextView tvEventDetailsDescription;
    TextView tvEventDetailsStartDate;
    TextView tvEventDetailsEndDate;
    TextView tvEventDetailsAddress;
    TextView tvEventDetailsSite;
    TextView tvEventDetailsCost;
    ImageView ivEventDetailsImage;
    Button btnPhotoAlbum;
    ImageView ivBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Event Details");
        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));

        tvEventDetailsTitle = findViewById(R.id.tvEventDetailsTitle);
        tvEventDetailsDescription = findViewById(R.id.tvEventDetailsDescription);
        tvEventDetailsStartDate = findViewById(R.id.tvEventDetailsStartDate);
        tvEventDetailsEndDate = findViewById(R.id.tvEventDetailsEndDate);
        tvEventDetailsAddress = findViewById(R.id.tvEventDetailsAddress);
        tvEventDetailsSite = findViewById(R.id.tvEventDetailsSite);
        tvEventDetailsCost = findViewById(R.id.tvEventDetailsCost);
        ivEventDetailsImage = findViewById(R.id.ivEventsDetailsImage);
        btnPhotoAlbum = findViewById(R.id.btnPhotoAlbum);
        ivBookmark = findViewById(R.id.ivBookmark);

        try {
            setDetailsScreenText();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        setUpDoubleTap();

        btnPhotoAlbum.setOnClickListener(v -> goPhotoAlbum());
    }

    private void setUpDoubleTap() {
        final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                try {
                    if (checkIfEventAlreadyBookmarked(event.getId()) == 1) {
                        removeBookmark(event.getId());
                    } else {
                        addEventToBookmarked();
                    }
                } catch (com.parse.ParseException ex) {
                    ex.printStackTrace();
                }
                return true;
            }
        };

        final GestureDetector detector = new GestureDetector(listener);
        detector.setOnDoubleTapListener(listener);
        getWindow().getDecorView().setOnTouchListener((view, event) -> detector.onTouchEvent(event));
    }

    private void removeBookmark(String eventId) {
        ParseQuery<Bookmark> query = ParseQuery.getQuery(Bookmark.class);
        query.whereEqualTo(Bookmark.KEY_BOOKMARKED_EVENT_ID, eventId);
        query.whereEqualTo(Bookmark.KEY_USER, ParseUser.getCurrentUser());
        query.getFirstInBackground((bookmarks, e) -> {
            if (e != null) {
                Toast.makeText(EventDetailsActivity.this, "Failed to remove bookmark", Toast.LENGTH_LONG).show();
            }
            try {
                bookmarks.delete();
            } catch (com.parse.ParseException ex) {
                ex.printStackTrace();
            }
            bookmarks.saveInBackground();
            Toast.makeText(EventDetailsActivity.this, "Removed bookmark!", Toast.LENGTH_SHORT).show();
            ivBookmark.setVisibility(View.INVISIBLE);
        });
    }

    private int checkIfEventAlreadyBookmarked(String eventId) throws com.parse.ParseException {
        ParseQuery<Bookmark> query = ParseQuery.getQuery(Bookmark.class);
        query.whereEqualTo(Bookmark.KEY_BOOKMARKED_EVENT_ID, eventId);
        query.whereEqualTo(Bookmark.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground((bookmarks, e) -> {
            if (e != null) {
                Toast.makeText(EventDetailsActivity.this, "Failed to check if already bookmarked", Toast.LENGTH_LONG).show();
                return;
            }
        });
        return query.count();
    }

    private void addEventToBookmarked() {
        String eventId = event.getId();
        String eventCategory = event.getCategory();
        ParseUser currentUser = ParseUser.getCurrentUser();
        saveBookmark(eventId, eventCategory, currentUser);
    }

    private void saveBookmark(String eventId, String eventCategory, ParseUser currentUser) {
        Bookmark bookmark = new Bookmark();
        bookmark.setEventId(eventId);
        bookmark.setEventCategory(eventCategory);
        bookmark.setUser(currentUser);
        bookmark.saveInBackground(e -> {
            if (e != null) {
                Toast.makeText(EventDetailsActivity.this, "Error saving bookmark!", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(EventDetailsActivity.this, "Bookmarked!", Toast.LENGTH_LONG).show();
            ivBookmark.setVisibility(View.VISIBLE);
        });
    }

    private void goPhotoAlbum() {
        Intent intent = new Intent(EventDetailsActivity.this, PhotoAlbumActivity.class);
        intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
        startActivity(intent);
    }

    private void setDetailsScreenText() throws com.parse.ParseException {
        tvEventDetailsTitle.setText(event.getName());
        tvEventDetailsDescription.setText(event.getDescription());
        formatAndSetEventURL();
        tvEventDetailsAddress.setText(event.getLocation());
        tvEventDetailsCost.setText(event.getCost());
        Glide.with(this).load(event.getImageUrl()).transform(new CenterCrop(), new RoundedCorners(30)).into(ivEventDetailsImage);
        setEventStartAndEndDates();
        if (checkIfEventAlreadyBookmarked(event.getId()) == 1) {
            ivBookmark.setVisibility(View.VISIBLE);
        }
    }

    private void setEventStartAndEndDates() {
        try {
            tvEventDetailsStartDate.setText(convertEventDateFormat(event.getTimeStart()));
        } catch (ParseException e) {
            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve start date", Toast.LENGTH_SHORT).show();
        }
        try {
            tvEventDetailsEndDate.setText(convertEventDateFormat(event.getTimeEnd()));
        } catch (ParseException e) {
            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve end date", Toast.LENGTH_SHORT).show();
        }
    }

    private void formatAndSetEventURL() {
        String link = event.getEventSiteUrl();
        tvEventDetailsSite.setMovementMethod(LinkMovementMethod.getInstance());
        tvEventDetailsSite.setClickable(true);
        String text = "<a href='" + link + "'> Book Tickets and Learn More </a>";
        tvEventDetailsSite.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private String convertEventDateFormat(String unformattedDate) throws ParseException {
        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date formattedDate = inputFormat.parse(unformattedDate);
        return outputFormat.format(formattedDate);
    }


}

