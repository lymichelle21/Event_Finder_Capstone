package com.example.event_finder_capstone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etZip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etZip = findViewById(R.id.etZip);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(v -> signUpUser());
    }

    private void signUpUser() {
        ParseUser user = new ParseUser();
        user.setUsername(etUsername.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.put("zip", etZip.getText().toString());

        user.signUpInBackground(e -> {
            if (e != null) {
                Toast.makeText(SignUpActivity.this, "Error: Sign up failed", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(SignUpActivity.this, "Sign up success!", Toast.LENGTH_LONG).show();
            goMainActivity();
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
