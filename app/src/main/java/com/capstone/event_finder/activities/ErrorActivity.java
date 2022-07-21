package com.capstone.event_finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.capstone.event_finder.R;
import com.capstone.event_finder.utils.ZipFormatCheck;
import com.parse.ParseUser;

public class ErrorActivity extends AppCompatActivity {

    TextView etZip;
    Button btnEditZip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        etZip = findViewById(R.id.etZip);
        btnEditZip = findViewById(R.id.btnEditZip);
        btnEditZip.setOnClickListener(v -> updateZip());
    }

    private void updateZip() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("zip", etZip.getText().toString());
        currentUser.saveInBackground((e) -> {
            if (e != null) {
                e.printStackTrace();
                Toast.makeText(ErrorActivity.this, "Failed to update zip", Toast.LENGTH_LONG).show();
                return;
            }
            if (ZipFormatCheck.isZipValidFormat(etZip.getText().toString(), ErrorActivity.this)) {
                goToMainActivity();
                Toast.makeText(ErrorActivity.this, "Updated zip!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
