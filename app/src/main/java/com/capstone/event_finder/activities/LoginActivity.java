package com.capstone.event_finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.capstone.event_finder.R;
import com.capstone.event_finder.network.EventViewModel;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        checkIfLoggedIn();

        btnLogin.setOnClickListener(v -> loginUser());

        btnSignUp.setOnClickListener(v -> signupUser());
    }

    private void signupUser() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void loginUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                Toast.makeText(LoginActivity.this, "Error: Login failed", Toast.LENGTH_LONG).show();
                return;
            }
            goMainActivity();
            Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_LONG).show();
        });
    }

    private void checkIfLoggedIn() {
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
