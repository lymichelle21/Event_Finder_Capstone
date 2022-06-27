package com.example.event_finder_capstone;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.event_finder_capstone.models.Event;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity {
    Context context;
    Event event;

    TextView tvEventDetailsTitle;
    TextView tvEventDetailsDescription;
    TextView tvEventDetailsStartDate;
    TextView tvEventDetailsEndDate;
    TextView tvEventDetailsAddress;
    TextView tvEventDetailsSite;
    TextView tvEventDetailsCost;
    ImageView ivEventDetailsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvEventDetailsTitle = (TextView) findViewById(R.id.tvEventDetailsTitle);
        tvEventDetailsDescription = (TextView) findViewById(R.id.tvEventDetailsDescription);
        tvEventDetailsStartDate = (TextView) findViewById(R.id.tvEventDetailsStartDate);
        tvEventDetailsEndDate = (TextView) findViewById(R.id.tvEventDetailsEndDate);
        tvEventDetailsAddress = (TextView) findViewById(R.id.tvEventDetailsAddress);
        tvEventDetailsSite = (TextView) findViewById(R.id.tvEventDetailsSite);
        tvEventDetailsCost = (TextView) findViewById(R.id.tvEventDetailsCost);
        ivEventDetailsImage = (ImageView) findViewById(R.id.ivEventsDetailsImage);

        event = (Event) Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));

        tvEventDetailsTitle.setText(event.getName());
        tvEventDetailsDescription.setText(event.getDescription());
        //TODO: Figure out why not populating
        tvEventDetailsSite.setText(event.getCategory());
        Glide.with(this).load(event.getImageUrl()).centerCrop().into(ivEventDetailsImage);
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

    private String convertEventDateFormat(String unformattedDate) throws ParseException {
        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date formattedDate = inputFormat.parse(unformattedDate);
        return outputFormat.format(formattedDate);
    }
}

