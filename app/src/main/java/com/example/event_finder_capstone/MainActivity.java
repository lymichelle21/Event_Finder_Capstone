package com.example.event_finder_capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.event_finder_capstone.models.Event;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getEvents();
    }

    private void getEvents() {
        Call<Event> call = RetrofitClient.getInstance().getYelpAPI().getEvents();
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                return;
            }
            @Override
            public void onFailure(Call<Event> call,  Throwable e) {
                Toast.makeText(getApplicationContext(), "An error has happened", Toast.LENGTH_LONG).show();
            }

        });
    }
}